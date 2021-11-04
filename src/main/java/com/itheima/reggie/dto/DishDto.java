package com.itheima.reggie.dto;


import com.itheima.reggie.pojo.Dish;
import com.itheima.reggie.pojo.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {
    //口味
    private List<DishFlavor> flavors = new ArrayList<>();
    //菜品名称
    private String categoryName;

    private Integer copies;
}
