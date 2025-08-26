package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     *根据菜品id查询关联套餐id
     */
    List<Long> getSetmealDishIdsByDishIds(List<Long> dishIds);

    /**
     * 根据菜品id更新相关套餐
     * @param setmeal
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 插入新增的套餐
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 删除套餐相关联的
     * @param setmealId
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteById(Long setmealId);

    /**
     * 根据id查询套餐，回显数据方便修改
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId} ")
    List<SetmealDish> getsetmealDishId(Long id);
}
