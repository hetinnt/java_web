package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Order;
import cn.itcast.travel.domain.Price;
import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {

    /**
     * 根据cid查询总记录数
     */
    public  int findTotalCount(int cid, String rname, Price price);

    /**
     * 根据cid，start，pageSize查询当前页数据集合
     */
    public List<Route> findByPage(int cid, int start, int pageSize, String rname, Order order,Price price);

    /**
     * 根据rid查询指定线路
     * @param rid
     * @return
     */
    public Route findByRid(int rid);

    /**
     * 更新收藏次数
     * @param rid
     */
    public void updateCount(int rid,int update_count);

}
