package com.miaoqy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miaoqy.dao.AddressBookDao;
import com.miaoqy.pojo.AddressBook;
import com.miaoqy.service.AddressBookService;
import org.springframework.stereotype.Controller;

@Controller
public class AddressBookServiceImpl extends ServiceImpl<AddressBookDao,AddressBook> implements AddressBookService {
}
