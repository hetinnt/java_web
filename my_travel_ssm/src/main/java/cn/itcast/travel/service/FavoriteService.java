package cn.itcast.travel.service;

import cn.itcast.travel.domain.Route;
import com.github.pagehelper.PageInfo;

public interface FavoriteService {

    /**
     * 判断是否收藏
     * @param rid
     * @param uid
     * @return
     */
    public boolean isFavorite(String rid, int uid);

    /**
     * 添加收藏
     * @param rid
     * @param uid
     */
    void add(String rid, int uid);

    /**
     * 我的收藏线路显示
     * @param uid
     * @return
     */
    PageInfo<Route> findFavoriteRoute(int uid, int currentPage, int pageSize);
}
