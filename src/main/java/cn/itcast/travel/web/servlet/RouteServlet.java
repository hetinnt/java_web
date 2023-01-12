package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.*;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet(name = "RouteServlet", value = "/route/*")
public class RouteServlet extends BaseServlet {
    
    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();
    
    /**
     * 分页查询.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接受参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");
        String orderStr = request.getParameter("order");
        String priceRangeStr = request.getParameter("priceRange");
        //接受rname
        String rname = request.getParameter("rname");
        rname = new String(rname.getBytes("iso-8859-1"),"utf-8");

        int cid = 0;//类别id
        //2.处理参数
        if(cidStr != null && cidStr.length()>0 && !"null".equals(cidStr)){
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 1;//当前页码，默认第一页
        if(currentPageStr != null && currentPageStr.length()>0){
            currentPage = Integer.parseInt(currentPageStr);
        }
        int pageSize = 5;//每页条数，默认5条
        if(pageSizeStr != null && pageSizeStr.length()>0){
            pageSize = Integer.parseInt(pageSizeStr);
        }

        Order order = null;
        if(orderStr != null && orderStr.length()>0 && !"null".equals(orderStr)){
            order = new Order();
            String[] s = orderStr.split("_");
            order.setType(s[0]);
            order.setA_d(s[1]);
        }

        Price price = new Price();
        if(priceRangeStr != null && priceRangeStr.length()> 0 && !"null".equals(priceRangeStr) && !"-".equals(priceRangeStr)){
            String[] split = priceRangeStr.split("-");
            price.setLowprice(Integer.parseInt(split[0]));
            price.setHighprice(Integer.parseInt(split[1]));
        }

        //3.调用service查询PageBean对象
        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, pageSize,rname,order,price);
        //4.将PageBean对象序列化为json，返回
        writeValue(pb,response);
    }

    /**
     * 根据id查询一个旅游线路的详细信息.
     * @param request
     * @param response
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.接收rid
        String rid = request.getParameter("rid");
        //2.调用service查询
        Route route = routeService.findOne(rid);
        //3.转换json返回
        writeValue(route,response);
    }

    /**
     * 判断当前登录用户是否收藏过该线路
     * @param request
     * @param response
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //1. 获取线路id
        String rid = request.getParameter("rid");

        //2. 获取当前登录的用户 user
        User user =(User) request.getSession().getAttribute("user");
        int uid;
        ResultInfo info = new ResultInfo();
        if(user == null){
            //用户尚未登录
            info.setFlag(false);
            info.setErrorMsg("尚未登录");
        }else{
            //用户已经登录
            uid = user.getUid();
            //3. 调用FavoriteService查询是否收藏
            boolean flag =favoriteService.isFavorite(rid, uid);
            info.setFlag(flag);
            if(flag){
                info.setErrorMsg("已收藏该线路");
            }else {
                info.setErrorMsg("未收藏该线路");
            }
        }

        //4. 写回客户端
        writeValue(info,response);
    }

    /**
     * 添加收藏.
     * @param request
     * @param response
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //1. 获取线路rid
        String rid = request.getParameter("rid");
        //2. 获取当前登录的用户
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户id
        if(user == null){
            //用户尚未登录
            return ;
        }else{
            //用户已经登录
            uid = user.getUid();
        }
        //3. 调用service添加
        favoriteService.add(rid,uid);
    }

    public void findUserFavorite(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //1.接受参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");

        int currentPage = 1;//当前页码，默认第一页
        if(currentPageStr != null && currentPageStr.length()>0){
            currentPage = Integer.parseInt(currentPageStr);
        }
        int pageSize = 5;//每页条数，默认5条
        if(pageSizeStr != null && pageSizeStr.length()>0){
            pageSize = Integer.parseInt(pageSizeStr);
        }

        //2. 获取当前登录的用户
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户id
        if(user == null){
            //用户尚未登录
            return;
        }else{
            //用户已经登录
            uid = user.getUid();
        }

        PageBean<Route> pb= favoriteService.findFavoriteRoute(uid,currentPage,pageSize);

        writeValue(pb,response);
    }
}
