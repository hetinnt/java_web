package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("categoryDao")
public class CategoryDaoImpl implements CategoryDao {

    @Autowired
    private JdbcTemplate template;

    /**
     * 查询所有分类条目
     * @return List<Category>
     */
    @Override
    public List<Category> findAll() {

        String sql = "select * from tab_category order by cid ASC ";
        return template.query(sql,new BeanPropertyRowMapper<Category>(Category.class));

    }
}
