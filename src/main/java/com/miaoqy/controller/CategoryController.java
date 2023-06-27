package com.miaoqy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miaoqy.common.R;
import com.miaoqy.pojo.Category;
import com.miaoqy.service.impl.CategoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryService;

    //新增菜品分类
    @PostMapping
    public R<String> insertNewDish(@RequestBody Category category){
        return categoryService.insert(category);
    }

    //分页
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        return categoryService.page(page,pageSize);
    }

    //删除菜品分类
    @DeleteMapping()
    public R<String> delete(Long ids){
        log.info("将要删除的分类id：{}",ids);
        categoryService.remove(ids);
        return R.success("分类删除成功");
    }

    //获取菜品分类
    @GetMapping("/list")
    public R<List<Category>> list(Category category){

        return categoryService.list(category);

    }

}
