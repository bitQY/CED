package com.miaoqy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.miaoqy.common.R;
import com.miaoqy.dto.SetmealDto;
import com.miaoqy.pojo.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    R<Page> page(Integer page, Integer pageSize, String name);
    void saveSetmeal(SetmealDto setmealDto);
    void deleteSetmeal(List<Long> ids);
    void updateStatus(Setmeal setmeal);
}
