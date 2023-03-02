package cn.itcast.travel.service.impl;

import cn.itcast.travel.domain.*;
import cn.itcast.travel.mapper.RouteImgMapper;
import cn.itcast.travel.mapper.RouteMapper;
import cn.itcast.travel.mapper.SellerMapper;
import cn.itcast.travel.service.RouteService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("routeService")
public class RouteServiceImpl implements RouteService {
    @Autowired
    private RouteMapper routeMapper;
    @Autowired
    private RouteImgMapper routeImgMapper;
    @Autowired
    private SellerMapper sellerMapper;

    @Override
    public PageInfo<Route> pageQuery(int cid, int currentPage, int pageSize, String rname,Order order,Price price) {

        //设置分页相关参数   当前页+每页显示的条数
        PageHelper.startPage(currentPage,pageSize);

        //设置当前显示的数据集合
        int start = (currentPage-1)*pageSize;//开始的记录数
        List<Route> routeList = routeMapper.findByPage(cid,rname,order,price);


        //获得与分页相关参数
        PageInfo<Route> pageInfo = new PageInfo<Route>(routeList);

        return pageInfo;
    }

    @Override
    public Route findOne(String rid) {
        //1.根据id去route表中查询route对象
        Route route = routeMapper.findByRid(Integer.parseInt(rid));
        //2.根据route的id 查询图片集合信息
        List<RouteImg> routeImgList = routeImgMapper.findByRid(route.getRid());
        //2.2将集合设置到route对象
        route.setRouteImgList(routeImgList);
        //3.根据route的sid（商家id）查询商家对象
        Seller seller = sellerMapper.findBySid(route.getSid());
        route.setSeller(seller);
        //4. 查询收藏次数
        return route;
    }
}
