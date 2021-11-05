package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.pojo.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.SendSms;
import com.itheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Description: test
 * @Create by: 草滩彭于晏
 * @Date:2021/11/5 16:50
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;


    /**
     * 用户登录发送验证码
     *
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R sendMsg(@RequestBody User user, HttpSession session) {
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            //生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //日志记录验证码
            log.info("code = {}", code);

            session.setAttribute(phone, code);
            return R.success("验证码发送成功" + code);
        }
        return R.error("验证码发送失败");
    }

    /**
     * 登录
     *
     * @param session
     * @param map
     * @return
     */
    @PostMapping("/login")
    public R login(HttpSession session, @RequestBody Map<String, String> map) {
        String code = map.get("code");
        String phone = map.get("phone");
        Object sessionCode = session.getAttribute(phone);
        if (sessionCode == null || !sessionCode.equals(code)) {
            return R.error("登录失败");
        }
        //移除session中的sessionCode
        session.removeAttribute(phone);
        //条件构造器
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("phone", phone);
        User user = userService.getOne(qw);
        //判断是否为新用户
        if (user == null) {
            //新用户
            user = new User();
            user.setPhone(phone);
            user.setStatus(1);
            //将用户存到表中
            userService.save(user);
        }
        //将user存到session中
        session.setAttribute("user", user.getId());
        return R.success(user);
    }
}






















