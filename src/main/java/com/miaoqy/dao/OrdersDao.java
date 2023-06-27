package com.miaoqy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miaoqy.common.BaseContext;
import com.miaoqy.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersDao extends BaseMapper<Orders> {
}
