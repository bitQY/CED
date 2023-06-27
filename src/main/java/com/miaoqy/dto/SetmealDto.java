package com.miaoqy.dto;
import com.miaoqy.pojo.Setmeal;
import com.miaoqy.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;
    private String categoryName;
}
