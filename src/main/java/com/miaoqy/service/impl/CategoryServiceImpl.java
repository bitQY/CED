package com.miaoqy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miaoqy.common.CustomException;
import com.miaoqy.common.R;
import com.miaoqy.dao.CategoryDao;
import com.miaoqy.pojo.Category;
import com.miaoqy.pojo.Dish;
import com.miaoqy.pojo.Setmeal;
import com.miaoqy.service.CategoryService;
import com.miaoqy.service.DishService;
import com.miaoqy.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    public R<Category> getAll(){
        List<Category> categoryList = categoryDao.selectList(null);
        return null;
    }

    /**
     * 新增菜品分类
     * @param category
     * @return
     */
    public R<String> insert (Category category){
        int insert = categoryDao.insert(category);
        if (insert!=0){
            return R.success("新增菜品分类成功");
        }
        return R.error("添加失败");
    }

    /**
     * 分页
     * @param page
     * @param pageSize
     * @return
     */
    public R<Page> page(int page,int pageSize){
        Page<Category> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(Category::getSort);
        categoryDao.selectPage(pageInfo,lqw);
        return R.success(pageInfo);
    }

    /**
     * 删除菜品分类
     * @param id
     * @return
     */
    public R<String> delete(Long id){
        categoryDao.deleteById(id);
        return R.success("删除成功");
    }


    public void remove(Long id){
        LambdaQueryWrapper<Dish> dishlqw = new LambdaQueryWrapper<>();
        //添加dish查询条件，根据分类id进行查询
        dishlqw.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishlqw);
        //bebug用
        log.info("dish查询，查询到的条数为：{}",count1);
        //查看当前分类是否关联菜品，若已经关联，则抛出异常
        if (count1>0){
            throw new CustomException("当前分类关联了菜品，不能删除");
        }
        LambdaQueryWrapper<Setmeal> setmeallqw = new LambdaQueryWrapper<>();
        setmeallqw.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmeallqw);
        log.info("dish查询，查询到的条数为：{}",count2);
        if (count2>0){
            throw new CustomException("当前分类关联了菜品，不能删除");
        }
        super.removeById(id);
    }

    /**
     *  //获取菜品分类
     * @param category
     * @return
     */
    public R<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加条件，这里只需要判断是否为菜品（type为1是菜品，type为2是套餐）
        categoryLambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        //添加排序条件
        categoryLambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        //查询数据
        List<Category> list = this.list(categoryLambdaQueryWrapper);
        return R.success(list);
    }
}
