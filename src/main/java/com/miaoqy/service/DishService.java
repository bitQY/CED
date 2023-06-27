package com.miaoqy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.miaoqy.common.R;
import com.miaoqy.dto.DishDto;
import com.miaoqy.pojo.Dish;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);
    R<Page> getPage(Integer page, Integer pageSize, String name);
    R<DishDto> getByIdWithFlavor(Long id);
    void updateWithFlavor(DishDto dishDto);
    void updateStatus(Long ids,Integer scode);
    void deleteDish(Long ids);
    R<List<DishDto>> getDish(Dish dish);
}
