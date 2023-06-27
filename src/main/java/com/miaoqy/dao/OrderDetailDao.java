package com.miaoqy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miaoqy.pojo.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailDao extends BaseMapper<OrderDetail> {
}
