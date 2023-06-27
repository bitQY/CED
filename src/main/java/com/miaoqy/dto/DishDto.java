package com.miaoqy.dto;

import com.miaoqy.pojo.Dish;
import com.miaoqy.pojo.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors = new ArrayList<>();
    private String categoryName;
    private Integer copies;
}
