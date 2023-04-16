package com.itheima.reggie.controller.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.controller.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
//@WebFilter(filterName = "loginCheckFilter",urlPatterns ="/*")
public class LoginCheckFilter implements Filter {

    public static  final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        //1.获取本次请求URI
        String requestURI = httpServletRequest.getRequestURI();
        log.info("拦截到请求：{}",requestURI);

        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };
        //2.判断本次请求是否需要处理
        boolean check = check(urls,requestURI);

        //3.不需要处理，直接放行
        if(check){
            log.info("本次请求{}不需要处理",requestURI);
            chain.doFilter(request,response);
            return;
        }
        //4.判断登录状态
        if(httpServletRequest.getSession().getAttribute("employee")!=null){
            log.info("用户已登录，用户id为：{}",httpServletRequest.getSession().getAttribute("employee"));
            chain.doFilter(request,response);
            return;
        }

        log.info("用户未登录");
        //5、如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}