package com.tarnett.mapper;

import com.github.pagehelper.Page;
import com.tarnett.pojo.Route;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RouteMapper {

    Page<Route> selectPageByCid(@Param("cid") Integer cid, @Param("queryString") String queryString);

    List<Route> selectByCountLimit(Integer count);

    Route selectRouteAndSellerById(Integer rid);

    void updateCountAndByRid(Integer rid);

    void updateCountMinusByRid(Integer rid);

    List<Route> selectMyFavoriteRouteImageByUid(Integer uid);

    Page<Route> selectByCondition(@Param("queryString") String queryString);

    int insertRoute(Route route);

    void updateRoute(Route route);

    void updateIsDeleteById(@Param("rid")Integer rid);

    void updateRflagByAuto(@Param("rid")Integer rid,@Param("rflag")String rflag);
}
