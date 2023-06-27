package com.miaoqy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miaoqy.dao.OrderDetailDao;
import com.miaoqy.pojo.OrderDetail;
import com.miaoqy.service.OrderDetailService;
import org.springframework.stereotype.Controller;

@Controller
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailDao, OrderDetail> implements OrderDetailService {
}
