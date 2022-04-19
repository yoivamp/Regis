package com.regisbackend.regisbackend.dto;


import com.regisbackend.regisbackend.pojo.Dish;
import com.regisbackend.regisbackend.pojo.DishFlavor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 喵vamp
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
