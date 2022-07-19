package com.tarnett.mapper;

import com.github.pagehelper.Page;
import com.tarnett.pojo.Category;
import com.tarnett.pojo.Route;
import com.tarnett.pojo.Seller;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {

    public  List<Category> selectAll();

    Page<Category> selectByCondition(@Param("queryString") String queryString);

    int insertCategory(Category category);

    Category selectCategoryById(@Param("cid")Integer cid);

    void updateCategory(Category category);

    public void deleteCategory(@Param("cid")Integer cid);

    public List<Route> selectRouteByCid(@Param("cid")Integer cid);

    void updateOnline(@Param("cid")Integer cid, @Param("online")String online);
}
