package com.miaoqy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.miaoqy.pojo.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
