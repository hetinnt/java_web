package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet(name = "UserServlet", value = "/user/*") //user/add user/find
public class UserServlet extends BaseServlet {
    //声明UserService业务对象
    private UserService userService = new UserServiceImpl();

    public ResultInfo checkcode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //验证校验
        String check = request.getParameter("check");
        //从sesion中获取验证码
        HttpSession session = request.getSession();
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

    public void regist(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultInfo checkcode = checkcode(request, response);
        if(!checkcode.isFlag()){
            //将info对象序列化为json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(checkcode);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }
        //1.获取数据
        Map<String, String[]> map = request.getParameterMap();
        //2.封装对象
        User user =new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //3.调用service完成注册
        //UserService userService = new UserServiceImpl();
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
        writeValue(info,response);
    }

    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo checkcode = checkcode(request, response);
        if(!checkcode.isFlag()){
            //将info对象序列化为json
            writeValue(checkcode,response);
            return;
        }

        //1.获取用户名和密码数据
        Map<String, String[]> map = request.getParameterMap();
        //2.封装user对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3.调用service查询
        //UserService service = new UserServiceImpl();
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
            request.getSession().setAttribute("user",u);//登录成功标记
            //登录成功
            info.setFlag(true);
        }

        //响应数据
        writeValue(info,response);
    }

    public void findOne(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //从session中获取登录用户
        Object user = request.getSession().getAttribute("user");
        ResultInfo info = new ResultInfo();
        if(user == null){
            info.setFlag(false);
        }else {
            info.setFlag(true);
            info.setData(user);
        }
        //将user写回客户端
        writeValue(info,response);
    }

    public void exit(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //1.销毁session
        request.getSession().invalidate();

        //2.跳转登录页面
        response.sendRedirect(request.getContextPath()+"/login.html");
    }

    public void active(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //获取激活码
        String code = request.getParameter("code");
        if(code != null){
            //2.调用service完成激活
            //UserService service = new UserServiceImpl();
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
