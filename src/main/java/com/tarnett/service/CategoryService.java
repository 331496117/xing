package com.tarnett.service;

import com.tarnett.entity.PageResult;
import com.tarnett.entity.QueryPageBean;
import com.tarnett.pojo.Category;
import com.tarnett.pojo.Route;
import com.tarnett.pojo.Seller;

import java.util.List;

public interface CategoryService {

     List<Category> queryAll();

     PageResult<Category> queryPage(QueryPageBean queryPageBean);

     Category queryCategoryById(Integer cid);

     int addCategory(Category category);

     void modify(Category category);

     public void remove(Integer cid);

     List<Route> getRouteByCid(Integer cid);

    void updateOnline(Integer cid);
}
