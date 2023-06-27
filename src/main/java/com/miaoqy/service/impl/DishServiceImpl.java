package com.miaoqy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miaoqy.common.R;
import com.miaoqy.dao.CategoryDao;
import com.miaoqy.dao.DishDao;
import com.miaoqy.dao.DishFlavorDao;
import com.miaoqy.dto.DishDto;
import com.miaoqy.pojo.Category;
import com.miaoqy.pojo.Dish;
import com.miaoqy.pojo.DishFlavor;
import com.miaoqy.service.CategoryService;
import com.miaoqy.service.DishFlavorService;
import com.miaoqy.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishDao, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private DishFlavorDao dishFlavorDao;

    /**
     * 新增菜品
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //将菜品保存
        this.save(dishDto);
        //获取dishid
        Long dtoId = dishDto.getId();
        //将获取到的dishId赋值给dishFlavor的dishId属性
        List<DishFlavor> flavors = dishDto.getFlavors();
        System.out.println(flavors);
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dtoId);
        }
        //同时将菜品口味数据保存到dish_flavor表
        dishFlavorService.saveBatch(flavors);
    }

    /**
     *分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public R<Page> getPage(Integer page, Integer pageSize, String name){
        //构造分页构造器对象
        Page<Dish> dishPage = new Page<Dish>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<Dish>();
        //按名字模糊查询
        lambdaQueryWrapper.like(name!=null,Dish::getName,name);
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        this.page(dishPage);
        //对象拷贝，这里只需要拷贝一下查询到的条目数
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");
        //获取原records数据
        List<Dish> dishList = dishPage.getRecords();
        //遍历每一条records数据
        List<DishDto> list = dishList.stream().map((item)->{
            DishDto dishDto = new DishDto();
            //将数据赋给dishDto对象
            BeanUtils.copyProperties(item,dishDto);
            //然后获取一下dish对象的category_id属性
            Long categoryId = item.getCategoryId();
            //根据这个属性，获取到Category对象
            Category category =categoryDao.selectById(categoryId);
            //随后获取Category对象的name属性，也就是菜品分类名称
            //最后将菜品分类名称赋给dishDto对象就好了
            dishDto.setCategoryName(category.getName());
            //结果返回一个dishDto对象
            return dishDto;
            //并将dishDto对象封装成一个集合，作为我们的最终结果
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    /**
     * 修改菜品回显数据
     * @param id
     * @return
     */
    @Override
    public R<DishDto> getByIdWithFlavor(Long id) {

        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> flavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        dishDto.setFlavors(flavorList);
        return R.success(dishDto);
    }


    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新当前菜品数据（dish表）
        this.updateById(dishDto);
        //下面是更新当前菜品的口味数据
        //条件构造器
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 修改菜品状态
     * @param ids
     * @param scode
     */
    @Override
    public void updateStatus(Long ids, Integer scode) {
        Dish dish = this.getById(ids);
        dish.setStatus(scode);
        this.updateById(dish);
    }

    /**
     * 删除菜品
     * @param ids
     */
    @Override
    public void deleteDish(Long ids) {
        this.removeById(ids);
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,ids);
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
    }


    /**
     * 展示菜品
     * @param dish
     * @return
     */
    @Override
    public R<List<DishDto>> getDish(Dish dish) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        List<Dish> dishList = this.list(dishLambdaQueryWrapper);
        List<DishDto> collect = dishList.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryDao.selectById(categoryId);
            dishDto.setCategoryName(category.getName());
            Long id = item.getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, id);
            List<DishFlavor> dishFlavors = dishFlavorDao.selectList(dishFlavorLambdaQueryWrapper);
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(collect);
    }


}
