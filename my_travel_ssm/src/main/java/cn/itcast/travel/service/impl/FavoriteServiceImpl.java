package cn.itcast.travel.service.impl;

import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.mapper.FavoriteMapper;
import cn.itcast.travel.mapper.RouteMapper;
import cn.itcast.travel.service.FavoriteService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("favoriteService")
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;
    @Autowired
    private RouteMapper routeMapper;

    @Override
    public boolean isFavorite(String rid, int uid) {

        Favorite favorite = favoriteMapper.findByRidAndUid(Integer.parseInt(rid), uid);

        return favorite != null;//如果对象有值，则为true，反之，则为false
    }

    @Override
    public void add(String rid, int uid) {
        //收藏记录加入favorite表
        Favorite favorite =new Favorite();
        favorite.setDate(new Date().toString());
        favorite.setUser(new User(uid));
        favorite.setRoute(new Route(Integer.parseInt(rid)));
        favoriteMapper.add(favorite);
        //添加route收藏次数
        routeMapper.updateCount(Integer.parseInt(rid),1);
    }

    @Override
    public PageInfo<Route> findFavoriteRoute(int uid, int currentPage, int pageSize) {

        //设置分页相关参数   当前页+每页显示的条数
        PageHelper.startPage(currentPage,pageSize);

        //通过uid从favorite表查询收藏线路
        List<Integer> favoriteRoute = favoriteMapper.findFavoriteRoute(uid);
        if(favoriteRoute == null){
            return null;
        }

        List<Route> routeList = new ArrayList<>();
        for (int i = 0; i < favoriteRoute.size(); i++) {
            routeList.add(routeMapper.findByRid(favoriteRoute.get(i)));
        }
        PageInfo<Route> pb = new PageInfo<Route>(routeList);
        return pb;
    }
}
