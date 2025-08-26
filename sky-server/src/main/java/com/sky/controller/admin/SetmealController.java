package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/admin/setmeal")
@RestController
@Api(tags = "套餐相关接口")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @Transactional
    @ApiOperation("新增套餐")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐：{}",setmealDTO);
        setmealService.addWithDish(setmealDTO);
        return Result.success();
    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    @Transactional
    @ApiOperation("批量删除套餐")
    public Result delete(@RequestParam List<Long> ids){
        log.info("删除套餐：{}",ids);
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据id查询套餐，回显数据方便修改
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐，回显数据方便修改")
    public Result<SetmealVO> getById(@PathVariable Long id){
        SetmealVO setmealVO = setmealService.getByIdWithDishId(id);
        return Result.success(setmealVO);
    }

    /**
     * 编辑套餐信息
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @ApiOperation("编辑套餐信息")
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("编辑套餐信息：{}",setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐的起售或禁售
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("套餐的起售或禁售")
    public Result startOrStop(@PathVariable Integer status,Long id){
       log.info("套餐的起售或禁售：{}，{}",status,id);
       setmealService.startOrStop(status,id);
       return Result.success();
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult> pageResult(SetmealPageQueryDTO setmealPageQueryDTO){
            log.info("套餐分页查询：{}",setmealPageQueryDTO);
            PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
            return Result.success(pageResult);
    }

}
