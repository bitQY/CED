package com.miaoqy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miaoqy.dao.DishFlavorDao;
import com.miaoqy.dto.DishDto;
import com.miaoqy.pojo.DishFlavor;
import com.miaoqy.service.DishFlavorService;
import org.springframework.stereotype.Service;

import javax.xml.ws.WebServiceRef;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorDao, DishFlavor> implements DishFlavorService {

}
