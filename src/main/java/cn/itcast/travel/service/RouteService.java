package cn.itcast.travel.service;

import cn.itcast.travel.domain.Order;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Price;
import cn.itcast.travel.domain.Route;

/**
 * 线路Service
 */
public interface RouteService {
    /**
     * 根据cid分页查询旅游线路
     * @param cid
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname, Order order, Price price);

    /**
     * 根据rid查询对应路线
     * @param rid
     * @return
     */
    Route findOne(String rid);
}
