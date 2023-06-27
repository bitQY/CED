package com.miaoqy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miaoqy.common.R;
import com.miaoqy.dto.DishDto;
import com.miaoqy.pojo.Dish;
import com.miaoqy.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    //新增菜品
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info("接受到的数据 {}",dishDto);
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    //分页查询
    @GetMapping("/page")
    public R<Page> AllDish(Integer page, Integer pageSize, String name){
        return dishService.getPage(page, pageSize, name);
    }

    //修改菜品回显数据
    @GetMapping("/{id}")
    public R<DishDto> getOneDish(@PathVariable Long id){
        log.info("接受的数据id {}",id);
        return dishService.getByIdWithFlavor(id);
    }

    //修改菜品
    @PutMapping
    public R<String> updateDish(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功");
    }

    //修改菜品状态
    @PostMapping("status/{scode}")
    public R<String> updateStatus(Long ids,@PathVariable Integer scode){
        dishService.updateStatus(ids,scode);
        return R.success("修改成功");
    }
    //删除菜品
    @DeleteMapping
    public R<String> deleteDish(Long ids){
        dishService.deleteDish(ids);
        return R.success("删除成功");
    }

    //套餐管理里显示菜品
    @GetMapping("/list")
    public R<List<DishDto>> getDish(Dish dish){
        return dishService.getDish(dish);
    }

}
