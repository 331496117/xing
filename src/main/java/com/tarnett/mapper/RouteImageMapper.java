package com.tarnett.mapper;

import com.tarnett.pojo.RouteImg;

import java.util.List;

public interface RouteImageMapper {

    List<RouteImg> selectRouteImageByRid(Integer rid);

    List<RouteImg> selectMyFavoriteRouteImageByUid(Integer uid);
}
