package com.miaoqy.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
//照框架要求编写元数据对象处理器，在此类中统一对公共字段赋值
//实现接口之后，重写两个方法，一个是插入时填充，一个是修改时填充
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充(insert)...");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        //设置创建人id
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充(update)...");
        log.info(metaObject.toString());
        metaObject.setValue("updateTime",LocalDateTime.now());
        //设置更新人id
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
