package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.domain.Category;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CategoryDaoImpl implements CategoryDao {

    private JdbcTemplate template;
    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    /**
     * 查询所有分类条目
     * @return
     */
    @Override
    public List<Category> findAll() {

        String sql = "select * from tab_category order by cid ASC ";
        return template.query(sql,new BeanPropertyRowMapper<Category>(Category.class));

    }
}
