package com.miaoqy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miaoqy.dao.ShoppingCartDao;
import com.miaoqy.pojo.ShoppingCart;
import com.miaoqy.service.ShoppingCartService;
import org.springframework.stereotype.Controller;

@Controller
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartDao, ShoppingCart> implements ShoppingCartService {
}
