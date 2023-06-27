package com.miaoqy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.miaoqy.pojo.Orders;

public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
