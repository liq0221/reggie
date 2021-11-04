package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.pojo.Dish;
import com.itheima.reggie.pojo.DishFlavor;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: test
 * @Create by: 草滩彭于晏
 * @Date:2021/11/1 19:15
 */
@Service
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;


    /**
     * 保存菜品
     * @param dishDto
     */
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);
        save(dish);
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((flavor) -> {
            // 设置菜品口味表中的菜品id
            flavor.setDishId(dish.getId());
            return flavor;
        }).collect(Collectors.toList());

        //保存菜品口味
        dishFlavorService.saveBatch(flavors);

    }

    /**
     * 修改菜品
     * @param dishDto
     */
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        Dish dish =new Dish();
        BeanUtils.copyProperties(dishDto,dish);
        updateById(dishDto);
        //删除之前的口味参数
        QueryWrapper<DishFlavor> qw = new QueryWrapper<>();
        qw.eq("dish_id",dishDto.getId());
        dishFlavorService.remove(qw);

        //添加新口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getId());
            dishFlavorService.save(flavor);
        }
    }


}
