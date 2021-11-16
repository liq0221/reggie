package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.pojo.Category;
import com.itheima.reggie.pojo.Dish;
import com.itheima.reggie.pojo.DishFlavor;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
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
    //注入redisTemplate
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 起售状态修改(未完成)
     *
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R changeStatus(@PathVariable("status") Integer status, @RequestParam List<Long> ids) {

        if (status == 1) {
            QueryWrapper<Dish> qw = new QueryWrapper<>();
            qw.in("id", ids);
            List<Dish> dishes = dishService.list(qw);

        }

        return R.success("成功");
    }

    /**
     * 删除菜品
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R delete(@RequestParam List<Long> ids) {
        dishService.removeByIds(ids);
        return R.success("删除成功");
    }

    /**
     * 菜品分页查询
     *
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

    /**
     * 添加菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);

//        //清理所有分类下的菜品缓存
//        Set keys = redisTemplate.keys("dish_*"); 获取所有以dish_xxx开头的key
//        redisTemplate.delete(keys);

        //清理菜品分类下的缓存数据
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);

        return R.success("菜品添加成功");
    }


    /**
     * 根据id查找菜品信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Long id) {
        Dish dish = dishService.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        //查询口味
        LambdaQueryWrapper<DishFlavor> qw = new LambdaQueryWrapper<>();
        qw.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(qw);
        dishDto.setFlavors(flavors);
        return R.success(dishDto);
    }

    /**
     * 修改
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        //        //清理所有分类下的菜品缓存
//        Set keys = redisTemplate.keys("dish_*"); 获取所有以dish_xxx开头的key
//        redisTemplate.delete(keys);

        //清理菜品分类下的缓存数据
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);
        return R.success("成功");
    }


    /**
     * 套餐查询菜品种类
     *
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish) {
        List<Dish> dishDtoList = null;
        //动态构造key
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();
        //从redis中获取数据然后判断
        dishDtoList = (List<Dish>) redisTemplate.opsForValue().get(key);
        //redis中存在数据 返回
        if (dishDtoList != null) {
            return R.success(dishDtoList);
        }
        LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
        qw.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
//      qw.eq(StringUtils.isNotEmpty(dish.getCategoryId().toString()), Dish::getCategoryId, dish.getCategoryId());
        qw.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(qw);

        //将list中的dish换成dishDto
        dishDtoList = list.stream().map((dish1) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish1, dishDto);

            //获取菜品种类id
            Long categoryId = dish1.getCategoryId();

            //获取菜品种类
            Category category = categoryService.getById(categoryId);

            //判断菜品种类是否存在
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            Long id = dish1.getId();
            QueryWrapper<DishFlavor> qw1 = new QueryWrapper<>();
            qw1.eq("dish_id", id);

            // 根据菜品id 获取相应菜品口味
            List<DishFlavor> dishFlavorList = dishFlavorService.list(qw1);

            // 设置菜品的口味
            dishDto.setFlavors(dishFlavorList);

            return dishDto;
        }).collect(Collectors.toList());
        //如果不存在，需要查询数据库，将查询到的菜品数据缓存到Redis
        redisTemplate.opsForValue().set(key, dishDtoList, 60, TimeUnit.MINUTES);
        return R.success(dishDtoList);
    }


}
















