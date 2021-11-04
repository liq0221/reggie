package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.CommonException;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.pojo.Setmeal;

import java.util.List;

/**
 * @Description: test
 * @Create by: 草滩彭于晏
 * @Date:2021/11/1 19:11
 */
public interface SetMealService extends IService<Setmeal> {
    /**
     * 新增套餐 同时保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    void deleteWithDish(List<Long> ids) throws CommonException;
}
