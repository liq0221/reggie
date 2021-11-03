package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.pojo.Category;
import com.itheima.reggie.pojo.Dish;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: test
 * @Create by: 草滩彭于晏
 * @Date:2021/11/3 19:45
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R page(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<Dish>(page, pageSize);
        //条件构造器
        QueryWrapper<Dish> qw = new QueryWrapper<>();
        //设置条件
        qw.like(StringUtils.isNotEmpty(name), "name", name);
        qw.orderByDesc("update_time");
        //获取page
        Page<Dish> result = dishService.page(pageInfo, qw);
        //获取当前页面数据
        List<Dish> records = result.getRecords();
        //将数据添加菜品名称后,封装到新的集合中
        List<Dish> dishDtos = records.stream().map((dish) -> {
            DishDto dishDto = new DishDto();
            //对象拷贝
            BeanUtils.copyProperties(dish, dishDto);
            Long categoryId = dish.getCategoryId();
            Category category = categoryService.getById(categoryId);
            dishDto.setCategoryName(category.getName());
            return dishDto;
        }).collect(Collectors.toList());
        result.setRecords(dishDtos);
        return R.success(result);
    }

    @PostMapping
    public R save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("菜品添加成功");
    }

}
















