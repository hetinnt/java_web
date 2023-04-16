package com.itheima.reggie.controller.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.controller.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class CheckLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            // 登录录检查逻辑
            HttpSession session = request.getSession();
            Object loginUser = session.getAttribute("employee");
            if(loginUser !=null){
                //放行
                log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));
                return true;
            }
            // 拦截 如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
            log.info("用户未登录");
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
