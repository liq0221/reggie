package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.mapper.OrderMapper;
import com.itheima.reggie.pojo.*;
import com.itheima.reggie.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: test
 * @Create by: 草滩彭于晏
 * @Date:2021/11/1 15:20
 */
@Service
@Transactional
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 保存订单
     * @param orders
     */
    @Override
    public void saveOrder(Orders orders) {
        //当前用户id
        Long userId = BaseContext.getCurrentId();
        //获取购物车数据
        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        qw.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> list = shoppingCartService.list(qw);
        //获取用户地址信息
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        String detail = addressBook.getDetail();
        //地址
        orders.setAddress(detail);
        //电话
        orders.setPhone(addressBook.getPhone());
        //收货人
        orders.setConsignee(addressBook.getConsignee());

        BigDecimal amount = countAmount(list);
        //订单金额
        orders.setAmount(amount);
        //用户id
        orders.setUserId(userId);
        // 生成订单号
        orders.setNumber(IdWorker.getIdStr());
        //下单时间
        orders.setOrderTime(LocalDateTime.now());
        //用户名
        User user = userService.getById(userId);
        orders.setUserName(user.getName());
        //保存订单
        save(orders);

        //将购物车的信息拷贝到订单明细表中

        for (ShoppingCart shoppingCart : list) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailService.save(orderDetail);
        }

        //清空购物车数据
        shoppingCartService.remove(qw);


    }

    /**
     * 获取购物车总金额
     *
     * @param list
     */
    private BigDecimal countAmount(List<ShoppingCart> list) {
        BigDecimal bigDecimal = new BigDecimal(0.0);
        for (ShoppingCart shoppingCart : list) {
            Integer number = shoppingCart.getNumber();
            BigDecimal amount = shoppingCart.getAmount();
            BigDecimal multiply = amount.multiply(new BigDecimal(number));
            bigDecimal = bigDecimal.add(multiply);
        }
        return bigDecimal;
    }


}




















