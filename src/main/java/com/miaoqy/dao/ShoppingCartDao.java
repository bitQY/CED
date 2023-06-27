package com.miaoqy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miaoqy.pojo.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartDao extends BaseMapper<ShoppingCart> {
}
