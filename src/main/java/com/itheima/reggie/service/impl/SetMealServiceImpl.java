package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CommonException;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.mapper.SetMealMapper;
import com.itheima.reggie.pojo.Setmeal;
import com.itheima.reggie.pojo.SetmealDish;
import com.itheima.reggie.service.SetMealService;
import com.itheima.reggie.service.SetmealDishService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @Description: test
 * @Create by: 草滩彭于晏
 * @Date:2021/11/1 19:13
 */
@Service
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, Setmeal> implements SetMealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto, setmeal);
        save(setmeal);

        List<SetmealDish> list = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : list) {
            setmealDish.setSetmealId(setmeal.getId());
            setmealDishService.save(setmealDish);
        }

    }

    /**
     * 批量删除
     * @param ids
     */
    @Override
    public void deleteWithDish(List<Long> ids) {
        QueryWrapper<Setmeal> qw = new QueryWrapper<>();
        qw.in("id", ids);
        //筛选状态为1的套餐
        qw.eq("status", "1");
        //获取要删除的套餐
        int count = count(qw);
        if (count > 0) {
            throw new CommonException("套餐正在出售,不能删除!");
        }
        removeByIds(ids);

        //删除关系表中的数据 套餐和菜品之间的关联
        QueryWrapper<SetmealDish> qw1 = new QueryWrapper<>();
        qw1.in("setmeal_id", ids);
        setmealDishService.remove(qw1);
    }


}


















