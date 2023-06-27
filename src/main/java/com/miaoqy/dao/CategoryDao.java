package com.miaoqy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miaoqy.pojo.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryDao extends BaseMapper<Category> {
}
