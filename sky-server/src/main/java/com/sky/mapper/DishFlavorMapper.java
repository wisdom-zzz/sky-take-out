package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 菜品口味信息
 */
@Mapper
public interface DishFlavorMapper {

    /**
     * 新增菜品和口味数据
     * @param flavors
     */
    void insertDishFlavors(List<DishFlavor> flavors);

}
