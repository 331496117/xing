package com.tarnett.controller;

import com.tarnett.entity.Result;
import com.tarnett.pojo.Favorite;
import com.tarnett.pojo.User;
import com.tarnett.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @RequestMapping("/queryFavorite")
    private Result queryFavorite(Integer rid, HttpSession session){
        // 1. 判断用户是否登陆
        // 1.1 从session对象中获取登陆的用户
        User user = (User)session.getAttribute("loginUser");
        if(user==null){
            // 说明用户没有登陆
            return new Result(false,"");
        }
        // 2. 调用业务逻辑层查询数据
        Favorite favorite=favoriteService.queryFavorite(rid,user.getUid());
        // 3. 响应数据
        return new Result(true,"",favorite);
    }

    @RequestMapping("/addFavorite")
    public Result addFavorite(Integer rid,String returnUrl,HttpSession session){

        // 1. 判断用户是否登陆
        // 1.1 从session对象中获取登陆的用户
        User user = (User)session.getAttribute("loginUser");
        if(user==null){
            // 说明用户没有登陆
            return new Result(false,"您尚未登陆，请登陆后再操作!<a href='login.html?returnUrl="+returnUrl+"'>立即登录</a>");
        }
        // 调用业务逻辑层进行收藏操作
        try {
            favoriteService.addFavorite(rid,user.getUid());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"收藏失败，请稍后再试");
        }
        return new Result(true,"收藏成功");
    }

    @RequestMapping("/removeFavorite")
    public Result removeFavorite(Integer rid,HttpSession session){
        // 1. 判断用户是否登陆
        // 1.1 从session对象中获取登陆的用户
        User user = (User)session.getAttribute("loginUser");
        try {
            favoriteService.removeFavorite(rid,user.getUid());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"取消收藏失败，请稍后再试");
        }
        return new Result(true,"取消收藏成功");
    }

}
