package com.miaoqy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miaoqy.common.R;
import com.miaoqy.dao.EmployeeDao;
import com.miaoqy.pojo.Employee;
import com.miaoqy.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeDao, Employee> implements EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;

    /**
     * 登录
     * @param employee
     * @param session
     * @return
     */
    public R login(Employee employee,HttpSession session){
        //获取密码，MD5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //根据名字查询
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<Employee>();
        lambdaQueryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee employee1 = employeeDao.selectOne(lambdaQueryWrapper);
        //判断按名字查询是否成功
        if (employee1==null){
            return R.error("用户名错误或不存在");
        }
        //判断密码是否一致
        if (!employee1.getPassword().equals(password)){
            return R.error("密码错误");
        }
        //判断账号状态，是否被封禁
        if (employee1.getStatus()==0){
            return R.error("该账户已被禁用");
        }
        //名字和密码查询成功将id放入session中。
        session.setAttribute("employee",employee1.getId());
        return R.success(employee1);
    }

    /**
     * 退出登录
     * @param session
     * @return
     */
    public R<String> logout(HttpSession session){
        session.removeAttribute("employee");
        return R.success("退出成功");
    }


    /**
     * 新增员工
     * @param employee
     * @param session
     * @return
     */
    public R<String> insert(Employee employee,HttpSession session){
        //设置默认密码，MD5加密。
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //设置createTime和updateTime
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //根据session来获取创建人的id
        Object employee1 = session.getAttribute("employee");
        employee.setCreateUser((Long) employee1);
        employee.setUpdateUser((Long) employee1);
        employeeDao.insert(employee);
        return R.success("员工添加成功");
    }

    /**
     * 分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    public R<Page> page(int page,int pageSize,String name){
        //构造分页构造器
        Page<Employee> pageInfo = new Page<>(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        //添加过滤条件（当我们没有输入name时，就相当于查询所有了,输入name就模糊查询）
        lqw.like(!(name==null || "".equals(name)),Employee::getName,name);
        //并对查询的结果进根据更新时间降序排序
        lqw.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeDao.selectPage(pageInfo,lqw);
        return R.success(pageInfo);
    }

    /**
     * 禁用，启用员工账号状态
     * @param employee
     * @param session
     * @return
     */
    public R<String> update(Employee employee,HttpSession session){
        log.info("employee = {}",employee.toString());
        Object employee1 = session.getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser((Long) employee1);
        employeeDao.updateById(employee);
        return R.success("信息修改成功");
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public R<Employee> getById(Long id){
        Employee employee = employeeDao.selectById(id);
        return R.success(employee);
    }


}
