package cn.itcast.travel.mapper;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

public interface RouteImgMapper {
    /**
     * 根据route的id查询图片
     * @param rid
     * @return
     */
    public List<RouteImg> findByRid(int rid);
}
