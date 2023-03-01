package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    /**
     * 查询分类条目.
     *
     * <p>分类数据每一次页面加载后，都需要请求数据库来加载，对数据库压力大
     * 分类数据不会经常产生变化，所以最好使用redis缓存数据</p>
     * @return
     */
    @Override
    public List<Category> findAll() {
        //1.从redis中查询
        //1.1获取jedis客户端
        Jedis jedis = JedisUtil.getJedis();
        //1.2，使用redis的sortedset来使数据中存储的顺序就是展示的顺序
//        Set<String> categorys = jedis.zrange("category", 0, -1);
        //1.3查询sortedset中的分数(cid)和值(cname)
        Set<Tuple> categorys = jedis.zrangeWithScores("category", 0, -1);

        List<Category> cs = null;
        //2.判断查询的集合是否为空
        if(categorys== null || categorys.size()==0){
            System.out.println("从数据库查询....");
            //3.如果为空，需要从数据库查询，在将数据存入redis 
            //3.1从数据库查询
             cs = categoryDao.findAll();
             //3.2将集合数据存储到redis中的 category的key
            for (int i = 0; i < cs.size(); i++) {
                jedis.zadd("category",cs.get(i).getCid(),cs.get(i).getCname());
            }
            //归还连接
            jedis.close();
        }else {
            System.out.println("从redis中查询....");
            //4.如果不为空，将set数据存入list
            cs = new ArrayList<Category>();
            for (Tuple tuple : categorys) {
                Category category = new Category();
                category.setCname(tuple.getElement());
                category.setCid((int)tuple.getScore());
                cs.add(category);
            }
        }
        return cs;
    }

    /**
     * 查询分类条目2.
     *
     * 从数据库中按序查出，通过返回list的json序列，使顺序存入redis更好实现
     * @return
     */
    public String findAll2(){
        //1.先从redis中查询数据
        //1.1获取redis客户端连接
        Jedis jedis = JedisUtil.getJedis();
        String category_json = jedis.get("category_json");

        //2判断 category_json 数据是否为null
        if(category_json == null || category_json.length() == 0){
            //redis中没有数据
            System.out.println("redis中没数据，查询数据库...");
            //2.1从数据中查询
            List<Category> cs = categoryDao.findAll();
            //2.2将list序列化为json
            ObjectMapper mapper = new ObjectMapper();
            try {
                category_json = mapper.writeValueAsString(cs);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            //2.3 将json数据存入redis
            jedis.set("category_json",category_json);
            //归还连接
            jedis.close();
        }else{
            System.out.println("redis中有数据，查询缓存...");
        }


        return category_json;
    }
}
