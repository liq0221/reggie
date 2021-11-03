package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: test
 * @Create by: 草滩彭于晏
 * @Date:2021/10/29 19:18
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
