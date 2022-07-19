package com.tarnett.controller;

import com.tarnett.constant.MessageConstant;
import com.tarnett.entity.PageResult;
import com.tarnett.entity.QueryPageBean;
import com.tarnett.entity.Result;
import com.tarnett.pojo.Category;
import com.tarnett.pojo.Route;
import com.tarnett.pojo.Seller;
import com.tarnett.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/queryAll")
    public Result queryAll(){
        // 需要调用业务逻辑层查询数据
        List<Category> categoryList=categoryService.queryAll();
        return new Result(true,"",categoryList);
    }


    @RequestMapping("/addCategory")
    public Result addRoute(@RequestBody Category category){
        try {
            categoryService.addCategory(category);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_ROUTE_FAIL);
        }
        return new Result(true,MessageConstant.ADD_ROUTE_SUCCESS);
    }

    @RequestMapping("/queryPageManage")
    public PageResult<Category> queryPage(@RequestBody QueryPageBean queryPageBean){
        return categoryService.queryPage(queryPageBean);
    }

    @RequestMapping("/queryOne")
    public Result queryOne(Integer cid){
        try {
            Category category = categoryService.queryCategoryById(cid);
            return new Result(true,MessageConstant.QUERY_USER_SUCCESS,category);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_USER_FAIL);
        }
    }

    @RequestMapping("/modify")
    public Result modify(@RequestBody Category category){
        try {
            categoryService.modify(category);
        } catch (Exception e) {
            e.printStackTrace();
            new Result(false, MessageConstant.UPDATE_USER_FAIL);
        }
        return new Result(true,MessageConstant.UPDATE_USER_SUCCESS);
    }

    @RequestMapping("/remove")
    public Result remove(Integer cid){
        try {
            categoryService.remove(cid);
            return new Result(true,MessageConstant.REMOVE_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.REMOVE_USER_FAIL);
        }
    }

    @RequestMapping("/queryRoute")
    public Result queryRoute(Integer cid){
        // 通过 cid 在 tab_category_route 表中获取 获取 rid
        // 通过 rid 在tab_route 表中 获取旅游产品
        try {
            List<Route> route = categoryService.getRouteByCid(cid);
            return new Result(true,MessageConstant.QUERY_ROUTE_SUCCESS,route);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ROUTE_FAIL);
        }
    }
    @RequestMapping("/updateOnline")
    public Result updateOnline(Integer cid) {
        try {
            categoryService.updateOnline(cid);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.UPDATE_CATEGORY_ONLINE_FAIL);
        }
        return new Result(true, MessageConstant.UPDATE_CATEGORY_ONLINE_SUCCESS);
    }

}
