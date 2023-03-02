package cn.itcast.travel.controller;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.*;
import java.io.IOException;

@Controller
@RequestMapping("/user")
public class UserController{
    //声明UserService业务对象
    @Autowired
    private UserService userService;


    public ResultInfo checkcode(HttpSession session, String check){
        //验证校验
        //从sesion中获取验证码
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");//为了验证码只能使用一次
        ResultInfo info = new ResultInfo();
        //比较
        if(checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)){
            //验证码错误
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            return info;
        }
        info.setFlag(true);
        return info;
    }

    @RequestMapping("/regist")
    @ResponseBody
    public ResultInfo regist(User user, HttpSession session,String check){
        ResultInfo checkcode = checkcode(session,check);
        if(!checkcode.isFlag()){
            //将info对象序列化为json
            return checkcode;
        }

        //1.获取数据

        //3.调用service完成注册
        //UserMapper userService = new UserServiceImpl();
        boolean flag = userService.regist(user);
        ResultInfo info = new ResultInfo();
        //4.响应结果
        if(flag){
            //注册成功
            info.setFlag(true);
        }else {
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败！");
        }

        //将info对象序列化为json并写回客户端
        return info;
    }

    @RequestMapping("/login")
    @ResponseBody
    public ResultInfo login(User user, HttpSession session,String check){
        ResultInfo checkcode = checkcode(session,check);
        if(!checkcode.isFlag()){
            //将info对象序列化为json
            return checkcode;
        }

        //1.获取用户名和密码数据

        //3.调用service查询
        User u = userService.login(user);

        ResultInfo info = new ResultInfo();

        //4.判断用户对象是否为null
        if(u == null){
            //用户名密码错误
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }
        //5.判断用户是否激活
        if(u != null && !"Y".equals(u.getStatus())){
            //用户尚未激活
            info.setFlag(false);
            info.setErrorMsg("您尚未激活，请激活");
        }
        //6.判断登录成功
        if(u != null && "Y".equals(u.getStatus())){
            session.setAttribute("user",u);//登录成功标记
            //登录成功
            info.setFlag(true);
        }

        //响应数据
        return checkcode;
    }

    @RequestMapping("/findOne")
    @ResponseBody
    public ResultInfo findOne(HttpSession session){
        //从session中获取登录用户
        Object user = session.getAttribute("user");
        ResultInfo info = new ResultInfo();
        if(user == null){
            info.setFlag(false);
        }else {
            info.setFlag(true);
            info.setData(user);
        }
        //将user写回客户端
        return info;
    }

    @RequestMapping("/exit")
    public String exit(HttpSession session){
        //1.销毁session
        session.invalidate();

        //2.跳转登录页面
        return "redirect:/index.html";
    }

    @RequestMapping("/active")
    public void active(String code, HttpServletResponse response) throws IOException{
        //获取激活码
        if(code != null){
            //2.调用service完成激活
            //UserMapper service = new UserServiceImpl();
            boolean flag = userService.active(code);

            //3.判断标记
            String msg=null;
            if(flag){
                //激活成功
                msg = "激活成功,请<a href='../login.html'>登录</a>";
            }else {
                //激活失败
                msg = "激活成功,请联系管理员！";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }
}
