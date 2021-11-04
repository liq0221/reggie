package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.pojo.Category;
import com.itheima.reggie.pojo.Dish;
import com.itheima.reggie.pojo.Setmeal;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: test
 * @Create by: 草滩彭于晏
 * @Date:2021/11/1 15:21
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    DishService dishService;
    @Autowired
    SetMealService setMealService;

    @PutMapping
    public R update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功");
    }
    /**
     * 新增分类
     *
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("新增分类成功");
    }


    /**
     * 菜品种类分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R page(int page, int pageSize) {
        //分页选择器
        IPage<Category> pageInfo = new Page<>(page, pageSize);
        //条件构造器
        QueryWrapper<Category> qw = new QueryWrapper<>();
        qw.orderByAsc("sort");
        //查询结果
        IPage<Category> page1 = categoryService.page(pageInfo, qw);
        return R.success(page1);

    }

    /**
     * 删除套餐
     * @param id
     * @return
     */
    @DeleteMapping
    public R delete(Long id) {
        QueryWrapper<Category> qw = new QueryWrapper<>();
        qw.eq("id", id);
        Category category = categoryService.getOne(qw);
        if (category.getType() == 1) {
            //是菜品分类
            QueryWrapper<Dish> qw1 = new QueryWrapper<>();
            qw1.eq("category_id", category.getId());
            int count = dishService.count(qw1);
            if (count > 0) {
                //有菜品 不能删
                return R.error("还有菜,不能删");
            }
        }

        if (category.getType() == 2) {
            //套餐分类
            QueryWrapper<Setmeal> qw2 = new QueryWrapper<>();
            qw2.eq("category_id", category.getId());
            int count = setMealService.count(qw2);
            if (count > 0) {
                //有套餐 不能删
                return R.error("有套餐 不能删");
            }
        }
        categoryService.removeById(id);
        return R.success("删除成功");
    }

    /**
     * 查询菜品种类(下拉列表)
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        QueryWrapper<Category> qw = new QueryWrapper<>();
        qw.eq("type", category.getType());
        qw.orderByAsc("sort").orderByDesc("update_time");
        List<Category> list = categoryService.list(qw);
        return R.success(list);

    }
}



























































