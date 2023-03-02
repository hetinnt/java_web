package cn.itcast.travel.mapper;

import cn.itcast.travel.domain.Category;

import java.util.List;

public interface CategoryMapper {
    /**
     * 查询分类条目.
     *
     * <p>分类数据每一次页面加载后，都需要请求数据库来加载，对数据库压力大
     * 分类数据不会经常产生变化，所以最好使用redis缓存数据</p>
     * @return
     */
    public List<Category> findAll();
}
