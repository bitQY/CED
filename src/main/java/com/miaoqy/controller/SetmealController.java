package com.miaoqy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miaoqy.common.R;
import com.miaoqy.dto.SetmealDto;
import com.miaoqy.pojo.Setmeal;
import com.miaoqy.service.SetmealDishService;
import com.miaoqy.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String name) {
        return setmealService.page(page,pageSize,name);
    }

    /**
     *新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveSetmeal(setmealDto);
        return R.success("添加成功");
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteSetmeal(@RequestParam List<Long> ids){
        setmealService.deleteSetmeal(ids);
        return R.success("删除成功");
    }

    /**
     * 修改套餐状态
     * @param sid
     * @param ids
     * @return
     */
    @PostMapping("/status/{sid}")
    public R<String> updateStatus(@PathVariable int sid,Long ids){
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(sid);
        setmeal.setId(ids);
        setmealService.updateStatus(setmeal);
        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,setmeal.getCategoryId());
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus,setmeal.getStatus());
        List<Setmeal> list = setmealService.list(setmealLambdaQueryWrapper);
        return R.success(list);
    }


}
