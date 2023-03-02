package cn.itcast.travel.mapper;

import cn.itcast.travel.domain.Seller;

public interface SellerMapper {
    /**
     * 根据sid查询
     * @param sid
     * @return
     */
    public Seller findBySid(int sid);
}
