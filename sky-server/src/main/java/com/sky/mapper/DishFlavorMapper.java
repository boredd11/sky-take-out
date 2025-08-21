package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     *向品味列表中添加n条品味
     */
    void insertBatch(List<DishFlavor> dishFlavors);
}
