package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.pojo.Orders;

/**
 * @Description: test
 * @Create by: 草滩彭于晏
 * @Date:2021/11/1 15:19
 */
public interface OrderService extends IService<Orders> {

    void saveOrder(Orders orders);
}
