package com.miaoqy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miaoqy.common.BaseContext;
import com.miaoqy.common.CustomException;
import com.miaoqy.dao.OrdersDao;
import com.miaoqy.pojo.*;
import com.miaoqy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Controller
public class OrdersServiceImpl extends ServiceImpl<OrdersDao, Orders> implements OrdersService {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     * @param orders
     */
    @Override
    public void submit(Orders orders) {
        //获取用户id
        Long id = BaseContext.getCurrentId();
        //根据用户id查询其购物车数据
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(id!=null,ShoppingCart::getUserId,id);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        //判断一下购物车是否为空
        if (shoppingCartList ==null){
            throw new CustomException("订单为空，不能下单");
        }
        //判断一下地址是否有误
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook==null){
            throw new CustomException("地址有误！无法下单");
        }
        //获取用户信息，为了后面赋值
        User user = userService.getById(id);
        Long orderId = IdWorker.getId();
        orders.setId(orderId);
        //算总金额
        AtomicInteger amount = new AtomicInteger(0);
        //向订单细节表设置属性
        List<OrderDetail> orderDetails = shoppingCartList.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setNumber(item.getNumber());
            orderDetail.setAmount(item.getAmount());
            //==单价*数量，最后转换为int
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        //向订单表设置属性
        orders.setId(orderId);
        orders.setNumber(String.valueOf(orderId));
        orders.setStatus(2);
        orders.setUserId(id);
        orders.setAddressBookId(addressBookId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setPhone(addressBook.getPhone());
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setAddress(
                (addressBook.getProvinceName() == null ? "":addressBook.getProvinceName())+
                        (addressBook.getCityName() == null ? "":addressBook.getCityName())+
                        (addressBook.getDistrictName() == null ? "":addressBook.getDistrictName())+
                        (addressBook.getDetail() == null ? "":addressBook.getDetail())
        );
        //根据查询到的购物车数据，对订单表插入数据（1条）
        this.save(orders);
        //根据查询到的购物车数据，对订单明细表插入数据（多条）
        orderDetailService.saveBatch(orderDetails);
        //清空购物车数据
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
    }
}
