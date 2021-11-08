package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.pojo.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: test
 * @Create by: 草滩彭于晏
 * @Date:2021/11/6 15:26
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R save(@RequestBody ShoppingCart shoppingCart) {
        //设置用户id
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        //判断添加的是菜品或者套餐
        Long dishId = shoppingCart.getDishId();
        //条件构造器
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, userId);
        if (dishId != null) {
            //添加菜品
            lqw.eq(ShoppingCart::getDishId, dishId);
        } else {
            //添加套餐
            lqw.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        //通过条件构造器查找购物车
        ShoppingCart cartServiceOne = shoppingCartService.getOne(lqw);
        if (cartServiceOne != null) {
            //购物车有菜品/套餐
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            //购物车无菜品/套餐
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }
        return R.success(cartServiceOne);
    }

    /**
     * 查询
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        qw.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> list = shoppingCartService.list(qw);
        return R.success(list);
    }

    /**
     * 删除
     *
     * @return
     */
    @DeleteMapping("/clean")
    public R clean() {
        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        qw.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(qw);
        return R.success("删除成功");
    }

    /**
     * 购物车中删减菜品或者套餐
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R sub(@RequestBody ShoppingCart shoppingCart) {
        //获取当前用户id
        Long userId = BaseContext.getCurrentId();
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        //条件构造器
        QueryWrapper<ShoppingCart> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);

        //判断是否为套餐或者菜品
        if (dishId != null) {
            qw.eq("dish_id", dishId);
        }
        if (setmealId != null) {
            qw.eq("setmeal_id", setmealId);
        }

        ShoppingCart serviceOne = shoppingCartService.getOne(qw);
        serviceOne.setNumber(serviceOne.getNumber() - 1);
        if (serviceOne.getNumber() == 0) {
            //说明没有菜品或者套餐了
            shoppingCartService.removeById(serviceOne.getId());
        } else {
            shoppingCartService.updateById(serviceOne);
        }

        return R.success("减少成功");
    }

}







































