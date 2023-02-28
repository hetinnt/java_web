package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.*;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao;
    private RouteImgDao routeImgDao;
    private SellerDao sellerDao;
    private FavoriteDao favoriteDao;

    public void setRouteDao(RouteDao routeDao) {
        this.routeDao = routeDao;
    }
    public void setRouteImgDao(RouteImgDao routeImgDao) {
        this.routeImgDao = routeImgDao;
    }
    public void setSellerDao(SellerDao sellerDao) {
        this.sellerDao = sellerDao;
    }
    public void setFavoriteDao(FavoriteDao favoriteDao) {
        this.favoriteDao = favoriteDao;
    }

    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname,Order order,Price price) {
        //封装PageBean
        PageBean<Route> pb = new PageBean<Route>();
        //设置当前页码和每页显示条数
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);

        //设置总记录数
        int totalCount = routeDao.findTotalCount(cid,rname,price);
        pb.setTotalCount(totalCount);
        //设置当前显示的数据集合
        int start = (currentPage-1)*pageSize;//开始的记录数
        List<Route> list = routeDao.findByPage(cid, start, pageSize, rname,order,price);
        pb.setList(list);

        //设置总页数 = 总记录数/每页显示条数
        int totalPage = (int)Math.ceil((totalCount*1.0)/pageSize);
        //int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize :(totalCount / pageSize) + 1 ;
        pb.setTotalPage(totalPage);

        return pb;
    }

    @Override
    public Route findOne(String rid) {
        //1.根据id去route表中查询route对象
        Route route = routeDao.findByRid(Integer.parseInt(rid));
        //2.根据route的id 查询图片集合信息
        List<RouteImg> routeImgList = routeImgDao.findByRid(route.getRid());
        //2.2将集合设置到route对象
        route.setRouteImgList(routeImgList);
        //3.根据route的sid（商家id）查询商家对象
        Seller seller = sellerDao.findBySid(route.getSid());
        route.setSeller(seller);

        //4. 查询收藏次数
/*        int count = favoriteDao.findCountByRid(route.getRid());
        route.setCount(count);*/

        return route;
    }
}
