package com.tarnett.service.impl;

import com.tarnett.mapper.FavoriteMapper;
import com.tarnett.mapper.RouteMapper;
import com.tarnett.pojo.Favorite;
import com.tarnett.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;
    @Autowired
    private RouteMapper routeMapper;

    @Override
    public Favorite queryFavorite(Integer rid, Integer uid) {
        return favoriteMapper.selectByRIdAndUid(rid,uid);
    }

    @Override
    public void addFavorite(Integer rid, Integer uid) {
        // 1. 往 tab_favorite表中添加一条记录
        // 构建一个 favorite 对象
        Favorite favorite=new Favorite();
        favorite.setRid(rid);
        favorite.setUid(uid);
        // 需要对日期进行格式化 yyyy-mm-dd HH:mm:ss
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = format.format(new Date());
        favorite.setDate(dateStr);
        favoriteMapper.insert(favorite);
        // 2. 更改tab_route表中该产品的收藏次数
        routeMapper.updateCountAndByRid(rid);
    }

    @Override
    public void removeFavorite(Integer rid, Integer uid) {
        favoriteMapper.deleteByRid(rid,uid);
        routeMapper.updateCountMinusByRid(rid);
    }
}
