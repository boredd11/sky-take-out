package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     *新增餐品样式
     */
    @PostMapping
    @ApiOperation("新增餐品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜单：{}",dishDTO);
        dishService.saveWithFlavors(dishDTO);
        return Result.success();
    }

    /**
     *菜品分页查询
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> pageResult(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询：{}",dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     *删除菜品
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result deleteById(@RequestParam List<Long> ids){
        log.info("批量删除菜品：{}",ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据id查询菜品
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getDishById(@PathVariable Long id){
        log.info("查询菜品：{}",id);
        DishVO dishVO = dishService.getDishById(id);
        return Result.success(dishVO);
    }

    /**
     *修改菜品
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品：{}",dishDTO);
        dishService.updateWithFlavor(dishDTO);
         return Result.success();
    }

    /**
     * 起售或停售菜品
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("起售或停售菜品")
    public Result startOrStop(@PathVariable Integer status,Long id){
            log.info("起售或停售菜品：{},{}",status,id);
            dishService.statrtOrStop(status,id);
            return Result.success();
    }

}
