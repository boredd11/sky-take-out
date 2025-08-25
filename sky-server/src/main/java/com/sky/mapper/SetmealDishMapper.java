package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

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
}
