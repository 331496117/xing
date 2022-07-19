package com.tarnett.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tarnett.entity.PageResult;
import com.tarnett.entity.QueryPageBean;
import com.tarnett.mapper.CategoryMapper;
import com.tarnett.pojo.Category;
import com.tarnett.pojo.Route;
import com.tarnett.pojo.Seller;
import com.tarnett.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public List<Category> queryAll(){
        // 1. 查询redis缓存中是否有分类的数据
        Jedis jedis = jedisPool.getResource();
        Set<Tuple> category = jedis.zrangeWithScores("category", 0, -1);
        // 2. 如果缓存中存在数据，则直接返回数据，不需要查询数据库
        List<Category> categoryList=new ArrayList<>();
        if(category!=null&&category.size()>0){
            // 说明缓存中存在数据，需要对数据类型进行转换
            for (Tuple tuple : category) {
                Category category1=new Category();
                category1.setCid((int)tuple.getScore());
                category1.setCname(tuple.getElement());
                categoryList.add(category1);
            }
            System.out.println("从redis缓存中查询");
            return categoryList;
        }
        // 3. 如果缓存中没有数据，则调用数据访问层去数据库查询数据
        categoryList=categoryMapper.selectAll();
        // 4. 将查询出来的数据保存到redis缓存中
        for (Category cate : categoryList) {
            jedis.zadd("category",cate.getCid(),cate.getCname());
        }

        // 5. 返回数据
        System.out.println("从数据库中查询");
        return categoryList;
    }


    @Override
    public PageResult<Category> queryPage(QueryPageBean queryPageBean) {
        // 设置 mybatis 的分页插件
        // 设置分页参数
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Category> pages = categoryMapper.selectByCondition(queryPageBean.getQueryString());
        return new PageResult<>(pages.getTotal(),pages.getResult());
    }

    @Override
    public Category queryCategoryById(Integer cid) {
        return categoryMapper.selectCategoryById(cid);
    }

    @Override
    public int addCategory(Category category) {
        return categoryMapper.insertCategory(category);
    }

    @Override
    public void modify(Category category) {
        categoryMapper.updateCategory(category);
    }

    @Override
    public void remove(Integer cid) {
        categoryMapper.deleteCategory(cid);
    }

    @Override
    public List<Route> getRouteByCid(Integer cid) {
        return categoryMapper.selectRouteByCid(cid);
    }

    @Override
    public void updateOnline(Integer cid) {
        String online = categoryMapper.selectCategoryById(cid).getOnline();
        online = "1".equals(online)? "0": "1";
        categoryMapper.updateOnline(cid, online);
    }
}
