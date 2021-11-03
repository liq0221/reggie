package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.pojo.Category;
import com.itheima.reggie.service.CategoryService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: test
 * @Create by: 草滩彭于晏
 * @Date:2021/11/1 15:18
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
