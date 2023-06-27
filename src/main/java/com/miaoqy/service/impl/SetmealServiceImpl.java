package com.miaoqy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miaoqy.common.CustomException;
import com.miaoqy.common.R;
import com.miaoqy.dao.CategoryDao;
import com.miaoqy.dao.SetmealDao;
import com.miaoqy.dao.SetmealDishDao;
import com.miaoqy.dto.SetmealDto;
import com.miaoqy.pojo.Category;
import com.miaoqy.pojo.Setmeal;
import com.miaoqy.pojo.SetmealDish;
import com.miaoqy.service.CategoryService;
import com.miaoqy.service.SetmealDishService;
import com.miaoqy.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealDao,Setmeal> implements SetmealService{

    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public R<Page> page(Integer page, Integer pageSize, String name) {
        //开启分页
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(name != null,Setmeal::getName,name);
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        this.page(setmealPage,setmealLambdaQueryWrapper);
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryDao.selectById(categoryId);
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }


    /**
     * 新建菜品
     * @param setmealDto
     */
    public void saveSetmeal(SetmealDto setmealDto){
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        List<SetmealDish> setmealDishList = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishList);
    }

    /**
     * 删除套餐
     * @param ids
     */
    @Override
    public void deleteSetmeal(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId,ids);
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(setmealLambdaQueryWrapper);
        log.info("查询到的数据为：{}",count);
        if (count>0){
            throw new CustomException("套餐正在售卖中，请先停售再进行删除");
        }
        this.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        //删除关系表中关联的菜品
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
    }

    /**
     * 修改套餐状态
     * @param setmeal
     */
    @Override
    public void updateStatus(Setmeal setmeal) {
        this.updateById(setmeal);
    }


}
