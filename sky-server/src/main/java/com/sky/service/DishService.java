package com.sky.service;

import com.sky.dto.DishDTO;

public interface DishService {
    /**
     *新增餐品样式
     */
    void saveWithFlavors(DishDTO dishDTO);
}
