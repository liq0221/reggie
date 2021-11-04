package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.pojo.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * @Description: test
 * @Create by: 草滩彭于晏
 * @Date:2021/10/29 19:22
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录
     *
     * @param employee
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpSession session) {
        //前端传递的用户名数据
        QueryWrapper<Employee> qw = new QueryWrapper<>();
        qw.eq("username", employee.getUsername());

        //数据库中查找到的用户数据
        Employee emp = employeeService.getOne(qw);
        if (emp == null) {
            return R.error("用户名不存在");
        }

        //用户名存在
        //密码加密,判断密码
        String res = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        if (!res.equals(emp.getPassword())) {
            return R.error("密码错误");
        }

        //密码正确
        //判断账户状态
        if (emp.getStatus() == 0) {
            return R.error("账户锁定,请联系管理员");
        }

        //账户正常
        //将用户数据存到session中
        session.setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 退出
     */
    @PostMapping("logout")
    public R logout(HttpSession session) {
        session.removeAttribute("empolyee");
        return R.success("退出成功");
    }


    /**
     * 保存
     *
     * @param employee
     * @param session
     * @return
     */
    @PostMapping
    public R save(@RequestBody Employee employee, HttpSession session) {
        //设置默认密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //获取当前账户的id
//        Long emp = (Long) session.getAttribute("employee");
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setCreateUser(emp);
//        employee.setUpdateUser(emp);

        employeeService.save(employee);
        return R.success("新增员工成功");
    }


    /**
     * @param page     当前页数
     * @param pageSize 当前页容量
     * @param name     查询条件
     * @return
     */
    @GetMapping("/page")
    public R page(Integer page, int pageSize, String name) {
        //构造分页构造器
        IPage<Employee> pageInfo = new Page(page, pageSize);
        //构造条件构造器
        QueryWrapper<Employee> qw = new QueryWrapper<>();
        // 添加过滤条件
        qw.like(StringUtils.isNotEmpty(name), "name", name);
        //添加排序条件
        qw.orderByDesc("create_time");
        //执行查询
        IPage<Employee> res = employeeService.page(pageInfo, qw);
        return R.success(res);
    }

    /**
     * 修改员工信息
     * @param employee
     * @param session
     * @return
     */
    @PutMapping
    public R update(@RequestBody Employee employee, HttpSession session) {
        //获取当前账户的ID
        Long empId = (Long) session.getAttribute("employee");
        //修改员工信息
//        employee.setUpdateUser(empId);
//        employee.setUpdateTime(LocalDateTime.now());

        employeeService.updateById(employee);
        return R.success("修改成功");
    }

    /**
     * 根据用户id查询数据 数据回显
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id) {
        Employee emp = employeeService.getById(id);
        return R.success(emp);
    }
}
































