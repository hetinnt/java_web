package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("favoriteService")
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteDao favoriteDao;
    @Autowired
    private RouteDao routeDao;

    @Override
    public boolean isFavorite(String rid, int uid) {

        Favorite favorite = favoriteDao.findByRidAndUid(Integer.parseInt(rid), uid);

        return favorite != null;//如果对象有值，则为true，反之，则为false
    }

    @Override
    public void add(String rid, int uid) {
        //收藏记录加入favorite表
        favoriteDao.add(Integer.parseInt(rid),uid);
        //添加route收藏次数
        routeDao.updateCount(Integer.parseInt(rid),1);
    }

    @Override
    public PageBean<Route> findFavoriteRoute(int uid,int currentPage, int pageSize) {
        PageBean<Route> pb = new PageBean<Route>();
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);

        //通过uid从favorite表查询收藏线路
        List<Integer> favoriteRoute = favoriteDao.findFavoriteRoute(uid);
        if(favoriteRoute == null){
            return null;
        }

        List<Route> routeList = new ArrayList<>();
        for (int i = 0; i < favoriteRoute.size(); i++) {
            routeList.add(routeDao.findByRid(favoriteRoute.get(i)));
        }
        //设置总记录数
        int totalCount = favoriteRoute.size();
        pb.setTotalCount(totalCount);

        //设置当前显示的数据集合
        int start = (currentPage-1)*pageSize;//开始的记录数
        int end = (start+pageSize)<routeList.size()?(start+pageSize):routeList.size();
        List<Route> list = routeList.subList(start,end);
        pb.setList(list);

        //设置总页数 = 总记录数/每页显示条数
        int totalPage = (int)Math.ceil((totalCount*1.0)/pageSize);
        //int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize :(totalCount / pageSize) + 1 ;
        pb.setTotalPage(totalPage);

        return pb;
    }
}
