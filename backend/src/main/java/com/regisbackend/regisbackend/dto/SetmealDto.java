package com.regisbackend.regisbackend.dto;


import com.regisbackend.regisbackend.pojo.Setmeal;
import com.regisbackend.regisbackend.pojo.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
