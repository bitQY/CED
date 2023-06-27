package com.miaoqy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miaoqy.common.R;
import com.miaoqy.pojo.Setmeal;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetmealDao extends BaseMapper<Setmeal> {
}
