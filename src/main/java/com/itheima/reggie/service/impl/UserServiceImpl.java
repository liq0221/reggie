package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.mapper.UserMapper;
import com.itheima.reggie.pojo.User;
import com.itheima.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Description: test
 * @Create by: 草滩彭于晏
 * @Date:2021/11/4 12:06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
