package com.tarnett.service;

import com.tarnett.entity.PageResult;
import com.tarnett.entity.QueryPageBean;
import com.tarnett.pojo.Route;
import com.tarnett.pojo.RouteImg;

import java.util.List;

public interface RouteService {
    PageResult<Route> queryPage(Integer cid, Integer currentPage,String queryString);

    List<Route> queryHot(Integer count);

    Route queryRouteAndSeller(Integer rid);
    
    List<RouteImg> selectRouteImageByRid(Integer rid);

    PageResult<Route> queryMyFavorite(Integer uid,Integer currentPage);

    PageResult<Route> queryPage(QueryPageBean queryPageBean);

    int addRoute(Route route);

    void modify(Route route);

    public void remove(Integer rid);

    void updateRflagByAuto(Integer rid);


}
