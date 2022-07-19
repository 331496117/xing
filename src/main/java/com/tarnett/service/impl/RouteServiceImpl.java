package com.tarnett.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tarnett.constant.RedisConstant;
import com.tarnett.entity.PageResult;
import com.tarnett.entity.QueryPageBean;
import com.tarnett.mapper.RouteImageMapper;
import com.tarnett.mapper.RouteMapper;
import com.tarnett.pojo.Route;
import com.tarnett.pojo.RouteImg;
import com.tarnett.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Service
public class RouteServiceImpl implements RouteService {
    @Autowired
    private RouteMapper routeMapper;

    @Autowired
    private RouteImageMapper routeImageMapper;

    @Autowired
    private JedisPool jedisPool;


    public PageResult<Route> queryPage(Integer cid, Integer currentPage,String queryString){
        // 1. 分页使用的是mybatis的分页插件 pageHelper
        // 2. 开启分页
        int pageSize=10;
        /**
         * 开启分页方法的两个参数:
         * pageNum: 当前页码就是 currentPage
         * pageSize：每页记录数 pageSize
         */
        PageHelper.startPage(currentPage,pageSize);
        // 3. 调用数据访问层查询数据
        // 组装模糊查询的参数
        queryString="%"+queryString+"%";
        Page<Route> pages=routeMapper.selectPageByCid(cid,queryString);
        // 4. 构造返回对象
        return new PageResult<Route>(pages.getTotal(),pages.getPages(),currentPage,pageSize,pages.getResult());
    }

    @Override
    public List<Route> queryHot(Integer count) {

        return routeMapper.selectByCountLimit(count);
    }

    @Override
    public Route queryRouteAndSeller(Integer rid) {
        return routeMapper.selectRouteAndSellerById(rid);
    }

    @Override
    public List<RouteImg> selectRouteImageByRid(Integer rid) {
        return routeImageMapper.selectRouteImageByRid(rid);
    }


    @Override
    public PageResult<Route> queryMyFavorite(Integer uid,Integer currentPage) {
        // 1. 分页使用的是mybatis的分页插件 pageHelper
        // 2. 开启分页
        int pageSize=8;
        /**
         * 开启分页方法的两个参数:
         * pageNum: 当前页码就是 currentPage
         * pageSize：每页记录数 pageSize
         */
        PageHelper.startPage(currentPage,pageSize);
        Page<Route> pages=(Page<Route>) routeMapper.selectMyFavoriteRouteImageByUid(uid);

        return new PageResult<>(pages.getTotal(),pages.getPages(),currentPage,pageSize,pages.getResult());
    }


    // *********************************


    @Override
    public PageResult<Route> queryPage(QueryPageBean queryPageBean) {
        // 设置mybatis 的分页插件
        // 设置分页
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        // 调用 mapper 查询数据
            Page<Route> pages = routeMapper.selectByCondition(queryPageBean.getQueryString());
        return new PageResult<>(pages.getTotal(),pages.getResult());
    }

    @Override
    public int addRoute(Route route) {
        int rows = routeMapper.insertRoute(route);
        // 将保存到数据库中的图片名称保存到 redis 中
        jedisPool.getResource().sadd(RedisConstant.ROUTE_IMG_DB,route.getRimage());
        return rows;
    }

    @Override
    public void modify(Route route) {
        routeMapper.updateRoute(route);
    }

    @Override
    public void remove(Integer rid) {
        routeMapper.updateIsDeleteById(rid);
    }

    @Override
    public void updateRflagByAuto(Integer rid) {
        String rflag = routeMapper.selectRouteAndSellerById(rid).getRflag();
        rflag=rflag.equals("1")?"0":"1";
        routeMapper.updateRflagByAuto(rid,rflag);
    }

}
