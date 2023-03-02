package cn.itcast.travel.mapper;

import cn.itcast.travel.domain.Order;
import cn.itcast.travel.domain.Price;
import cn.itcast.travel.domain.Route;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RouteMapper {
    /**
     * 根据cid查询总记录数
     */
    public  int findTotalCount(@Param("cid") int cid, @Param("rname")String rname, @Param("price")Price price);

    /**
     * 根据cid，start，pageSize查询当前页数据集合
     */
    public List<Route> findByPage(@Param("cid")int cid,  @Param("rname")String rname, @Param("order") Order order, @Param("price")Price price);

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
    public void updateCount(@Param("rid")int rid,@Param("update_count")int update_count);
}
