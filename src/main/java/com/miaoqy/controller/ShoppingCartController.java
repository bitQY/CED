package com.miaoqy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.miaoqy.common.BaseContext;
import com.miaoqy.common.R;
import com.miaoqy.pojo.ShoppingCart;
import com.miaoqy.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    public ShoppingCartService shoppingCartService;
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        //用户id
        Long currentId = BaseContext.getCurrentId();
        //放入shoppingCart
        shoppingCart.setUserId(currentId);
        //获取菜品id
        Long dishId = shoppingCart.getDishId();
        //获取套餐id
        Long setmealId = shoppingCart.getSetmealId();
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //判断添加的是菜品还是套餐
        if (dishId!=null){
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId,dishId);
        }
        else {
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId,setmealId);
        }
        ShoppingCart cartServiceOne = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
        //查询当前菜品或者套餐是否在购物车中
        if (cartServiceOne!=null){
            //如果已存在就在当前的数量上加1
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number+1);
            shoppingCartService.updateById(cartServiceOne);
        }
        else {
            shoppingCart.setCreateTime(LocalDateTime.now());
            //如果不存在，则添加到购物车，数量默认为1
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }
        return R.success(cartServiceOne);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        Long currentId = BaseContext.getCurrentId();
        shoppingCartLambdaQueryWrapper.eq(currentId!=null,ShoppingCart::getUserId,currentId);
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
        return R.success("清空购物车成功");
    }

}
