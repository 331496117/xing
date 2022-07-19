package com.tarnett.controller;

import com.tarnett.constant.MessageConstant;
import com.tarnett.constant.RedisConstant;
import com.tarnett.entity.PageResult;
import com.tarnett.entity.QueryPageBean;
import com.tarnett.entity.Result;
import com.tarnett.pojo.Route;
import com.tarnett.pojo.RouteImg;
import com.tarnett.pojo.User;
import com.tarnett.service.RouteService;
import com.tarnett.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import javax.mail.Multipart;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/queryPage")
    public PageResult<Route> queryPage(Integer cid,Integer currentPage,String queryString){
        // 1. 对获取的参数进行非空处理，如果Cid为null,那么应该查询所有分类下面所有的产品
        System.out.println(cid);
        if(cid==null){
            cid=0;
        }
        if(currentPage==null){
            currentPage=1;
        }
        // 2. 调用业务逻辑层的分页查询方法
        PageResult<Route> routePage=routeService.queryPage(cid,currentPage,queryString);

        return routePage;
    }

    @RequestMapping("/queryHot")
    public Result queryHot(@RequestParam(defaultValue = "5") Integer count){
        // 1. 对数据进行处理,这里使用springmvc提供的注解来实现
        // 2. 调用业务逻辑层查询数据
        List<Route> routeList=routeService.queryHot(count);
        // 3. 响应数据
        return new Result(true,"",routeList);
    }

    @RequestMapping("/queryRouteAndSeller")
    public Result queryRouteAndSeller(Integer rid){
        // 1. 调用业务逻辑层查询数据
        Route route=routeService.queryRouteAndSeller(rid);
        return new Result(true,"",route);
    }

    @RequestMapping("/queryRouteImage")
    public Result queryRouteImage(Integer rid){
        List<RouteImg> routeImgList =routeService.selectRouteImageByRid(rid);
        return new Result(true,"",routeImgList);
    }

    @RequestMapping("/queryMyFavorite")
    public PageResult<Route> queryMyFavorite(Integer currentPage,HttpSession session){
        User user = (User)session.getAttribute("loginUser");
        PageResult<Route> routePage= routeService.queryMyFavorite(user.getUid(),currentPage);
        System.out.println(routePage.getTotalPage());
        System.out.println(routePage);
        return routePage;
    }

    // *********************************

    @RequestMapping("/queryPageManage")
    public PageResult<Route> queryPage(@RequestBody QueryPageBean queryPageBean){
        return routeService.queryPage(queryPageBean);
    }

    @RequestMapping("/addRoute")
    public Result addRoute(@RequestBody Route route){
        try {
            routeService.addRoute(route);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_ROUTE_FAIL);
        }
        return new Result(true,MessageConstant.ADD_ROUTE_SUCCESS);
    }

    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile){
        // 获取上传文件的原名
        String originalFilename = imgFile.getOriginalFilename();
        // 组件文件名，UUID + 源文件
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + substring;
        try {
            // 调用七牛云的工具类上传图片
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            // 将上传到七牛云服务器上的图片名称保存到 redis 中
            jedisPool.getResource().sadd(RedisConstant.ROUTE_IMG_ALL,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
    }

    @RequestMapping("/queryOne")
    public Result queryOne(Integer rid){
        try {
            Route route = routeService.queryRouteAndSeller(rid);
            System.out.println(route.toString());
            return new Result(true,MessageConstant.QUERY_ROUTE_SUCCESS,route);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ROUTE_FAIL);
        }
    }

    @RequestMapping("/modify")
    public Result modify(@RequestBody Route route){
        try {
            routeService.modify(route);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.UPDATE_ROUTE_FAIL);
        }
        return new Result(true,MessageConstant.UPDATE_ROUTE_SUCCESS);
    }

    @RequestMapping("/remove")
    public Result remove(Integer rid){
        try {
            routeService.remove(rid);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.REMOVE_ROUTE_FAIL);
        }
        return new Result(true,MessageConstant.REMOVE_ROUTE_SUCCESS);
    }

    @RequestMapping("/updateFlag")
    public Result updateFlag(Integer rid){
        try {
            routeService.updateRflagByAuto(rid);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.UPDATE_ROUTE_FLAG_FAIL);
        }
        return new Result(true,MessageConstant.UPDATE_ROUTE_FLAG_SUCCESS);
    }

}
