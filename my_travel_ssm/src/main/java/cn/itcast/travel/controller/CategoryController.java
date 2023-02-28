package cn.itcast.travel.controller;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController{

    @Autowired
    private CategoryService service;

    /**
     * 查询所有分类条目
     */
    @RequestMapping("/findAll")
    @ResponseBody
    public List<Category> findAll(){
        //1.调用service查询所有
        List<Category> cs = service.findAll();
        //2.序列化json返回
        return cs;
    }
}
