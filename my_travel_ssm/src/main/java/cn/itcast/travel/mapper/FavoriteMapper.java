package cn.itcast.travel.mapper;

import cn.itcast.travel.domain.Favorite;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FavoriteMapper {

    /**
     * 根据rid和uid查询收藏信息
     * @param rid
     * @param uid
     * @return
     */
    Favorite findByRidAndUid(@Param("rid") int rid, @Param("uid") int uid);

    /**
     * 根据rid 查询收藏次数
     * @param rid
     * @return
     */
    int findCountByRid(int rid);

    /**
     * 添加收藏
     * @param favorite
     */
    void add(Favorite favorite);

    List<Integer> findFavoriteRoute(int uid);
}
