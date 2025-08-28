package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.charts.ScatterChartData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    /**
     *新增餐品样式
     */
    @Transactional
    public void saveWithFlavors(DishDTO dishDTO) {
        //向参评列表中添加一条餐品
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.insert(dish);

        //获取insert中的主键id
        Long dishId = dish.getId();
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        if(dishFlavors != null && !dishFlavors.isEmpty()){
            dishFlavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            //向品味列表中添加n条品味
            dishFlavorMapper.insertBatch(dishFlavors);
        }
    }

    /**
     *菜品分页查询
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     *删除菜品
     */
    public void deleteBatch(List<Long> ids) {
        //判断当前菜品是否能够删除----是否存在起售的菜品
        for(Long id:ids){
         Dish dish = dishMapper.getById(id);
         //在其起售中不能删除
         if(dish.getStatus() == StatusConstant.ENABLE){
             throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
         }
        }

        //判断当前菜品是否能够删除----是否被套餐关联
        List<Long> setmealDishIds = setmealDishMapper.getSetmealDishIdsByDishIds(ids);
        if(setmealDishIds != null && !setmealDishIds.isEmpty()){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //删除菜品中关联的口味数据
        dishFlavorMapper.deleteByDishIds(ids);
        //删除菜品表中的数据
        dishMapper.deleteByIds(ids);
    }

    /**
     * 根据id查询菜品
     */
    public DishVO getDishById(Long id) {
        //根据id查询菜品
        Dish dish = dishMapper.getById(id);
        //根据菜品id查询口味
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);
        //将dish封装到VO中
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /**
     *修改菜品
     */
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //修改菜品信息
        dishFlavorMapper.updateWithFlavor(dish);
        //删除菜品的口味
        dishFlavorMapper.deleteByDishIds(Collections.singletonList(dishDTO.getId()));
        //新增菜品的口味
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        if(dishFlavors != null && !dishFlavors.isEmpty()){
            dishFlavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            //向品味列表中添加n条品味
            dishFlavorMapper.insertBatch(dishFlavors);
        }
    }

    /**
     * 起售或停售菜品
     * @param status
     * @param id
     * @return
     */
    @Transactional
    public void statrtOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .status(status)
                .id(id)
                .build();
        dishMapper.update(dish);
        //菜品停售关联的菜品也要停售
        if(status == StatusConstant.DISABLE){
            //将查找的菜品id存入列表中
            List<Long> dishIds = new ArrayList<>();
            dishIds.add(id);
            //根据dishId找到setmealIds
            List<Long> setmealIds = setmealDishMapper.getSetmealDishIdsByDishIds(dishIds);
            if(setmealIds != null&& !setmealIds.isEmpty()){
                for(Long dishId : dishIds){
                    Setmeal setmeal = Setmeal.builder()
                            .status(status)
                            .id(id)
                            .build();
                   setmealDishMapper.update(setmeal);
                }
            }
        }
    }

    /**
     * 根据id查询套餐
     * @param categoryId
     * @return
     */
    public List<Dish> list(Long categoryId) {
        Dish dish = new Dish();
        Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
