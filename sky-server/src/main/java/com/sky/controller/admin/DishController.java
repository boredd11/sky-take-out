package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
