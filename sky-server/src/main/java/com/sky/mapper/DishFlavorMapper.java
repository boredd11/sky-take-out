package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     *向品味列表中添加n条品味
     */
    void insertBatch(List<DishFlavor> dishFlavors);

    /**
     *根据菜品删除相关关联的口味
     */
    void deleteByDishIds(List<Long> dishIds);

    /**
     *根据菜品id查询口味
     */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);

    /**
     *修改菜品
     */
    @AutoFill(value = OperationType.UPDATE)
    void updateWithFlavor(Dish dish);
}
