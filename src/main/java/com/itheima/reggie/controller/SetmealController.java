package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.pojo.Category;
import com.itheima.reggie.pojo.Setmeal;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetMealService;
import com.itheima.reggie.service.SetmealDishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @Description: test
 * @Create by: 草滩彭于晏
 * @Date:2021/11/4 12:07
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetMealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐功能
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);
        return R.success("成功");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R page(Integer page, Integer pageSize, String name) {
        IPage<Setmeal> pageInfo = new Page<Setmeal>(page, pageSize);
        //条件构造器
        QueryWrapper<Setmeal> qw = new QueryWrapper<>();
        qw.like(name != null, "name", name);
        qw.orderByDesc("update_time");
        IPage<Setmeal> result = setmealService.page(pageInfo, qw);

        List<Setmeal> collect = result.getRecords().stream().map((setmeal) -> {
            //拷贝对象
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal,setmealDto);
            //获取菜品种类
            Long categoryId = setmeal.getCategoryId();
            Category category = categoryService.getById(categoryId);
            //设置菜品种类名称
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());

        //更改page中的内容
        result.setRecords(collect);
        return R.success(result);
    }

    @DeleteMapping
    public R delete(@RequestParam List<Long> ids){
        setmealService.deleteWithDish(ids);
        return R.success("删除成功");
    }

}





















