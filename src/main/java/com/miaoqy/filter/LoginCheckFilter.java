package com.miaoqy.filter;

import com.alibaba.fastjson.JSON;
import com.miaoqy.common.BaseContext;
import com.miaoqy.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    //PathMatcher 路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request1 = (HttpServletRequest) request;
        HttpServletResponse response1 = (HttpServletResponse) response;


        //1.获取本次请求的URI
        String uri = request1.getRequestURI();
        log.info("拦截到的URI: {}", request1.getRequestURI());
        //定义不需要被拦截的请求
        String [] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                //放行登录
                "/user/login",
                "/user/sendMsg"
        };

        //2.判断本次请求是否需要处理
        boolean check = check(urls, uri);

        //3.如果不需要处理，则直接放行
        if (check){
            log.info("本次请求：{},不需要处理",uri);
            chain.doFilter(request1, response1);
            return;
        }

        //4.判断登录状态，如果已登录，则直接放行
        if (request1.getSession().getAttribute("employee")!=null){
            log.info("用户已登录，id为{}", request1.getSession().getAttribute("employee"));
            //在这里获取一下线程id
            long id = Thread.currentThread().getId();
            log.info("doFilter的线程id为：{}", id);
            //根据session来获取之前我们存的id值
            Long empId = (Long) request1.getSession().getAttribute("employee");
            //使用BaseContext封装id
            BaseContext.setCurrentId(empId);
            chain.doFilter(request1, response1);
            return;
        }

        //前端登录用户验证
        if (request1.getSession().getAttribute("user")!=null){
            log.info("用户登录，id为{}",request1.getSession().getAttribute("user"));
            Long userId = (Long)request1.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            chain.doFilter(request1,response1);
            return;
        }

        //5.如果未登录则返回未登录结果
        //“NOTLOGIN”配合request.js代码中的响应拦截器看
        log.info("用户未登录");
        log.info("id为:{}", request1.getSession().getAttribute("employee"));
        response1.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));







    }



    private boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url,requestURI);
            //如果匹配
            if (match) {return true;}
        }
        //如果不匹配
        return false;
    }
}
