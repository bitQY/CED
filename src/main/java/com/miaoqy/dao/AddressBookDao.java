package com.miaoqy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miaoqy.pojo.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookDao extends BaseMapper<AddressBook> {
}
