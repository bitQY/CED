package com.miaoqy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miaoqy.common.R;
import com.miaoqy.pojo.Employee;
import com.miaoqy.service.impl.EmployeeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeServiceImpl employeeService;

    //登录
    @PostMapping ("/login")
    public R login(@RequestBody Employee employee, HttpSession session){
        return employeeService.login(employee, session);
    }

    //退出
    @PostMapping("/logout")
    public R<String> logout(HttpSession session){
        return employeeService.logout(session);
    }

    //新增用户
    @PostMapping
    public R<String> save(@RequestBody Employee employee,HttpSession session){
        log.info("新增用户信息：{}",employee.toString());
        return employeeService.insert(employee,session);
    }

    //分页
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
        return employeeService.page(page,pageSize,name);
    }

    //禁用，启用员工账号状态
    @PutMapping
    public R<String> update(@RequestBody Employee employee,HttpSession session){
        return employeeService.update(employee,session);
    }

    //根据id查询
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        return employeeService.getById(id);
    }

}
