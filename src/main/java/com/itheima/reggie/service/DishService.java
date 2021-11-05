package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.pojo.Dish;

/**
 * @Description: test
 * @Create by: 草滩彭于晏
 * @Date:2021/11/1 19:14
 */
public interface DishService extends IService<Dish> {
    //新增菜品和口味
    void saveWithFlavor(DishDto dishDto);
    //修改菜品和口味
    void updateWithFlavor(DishDto dishDto);
}
