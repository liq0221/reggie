package com.itheima.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * @Description: test
 * @Create by: 草滩彭于晏
 * @Date:2021/11/1 11:22
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
//    @Autowired
//    private HttpSession session;

    /**
     * 插入操作时,自动填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        //当前账户id
//        Long empId = (Long) session.getAttribute("employee");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }

    /**
     * 修改操作时,自动填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
//        Long empId = (Long) session.getAttribute("employee");
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}



















