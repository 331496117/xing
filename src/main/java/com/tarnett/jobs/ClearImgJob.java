package com.tarnett.jobs;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.tarnett.constant.RedisConstant;
import com.tarnett.utils.QiniuUtils;

import redis.clients.jedis.JedisPool;

public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg() {

        System.out.println("------------------进入删除无效图片的定时任务------------------------");

        // 1. 根据redis保存的两个set集合进行差值计算，获得垃圾图片名称集合
        Set<String> imgSet = jedisPool.getResource().sdiff(RedisConstant.ROUTE_IMG_ALL, RedisConstant.ROUTE_IMG_DB);
        if(imgSet != null ) {
            for(String picName : imgSet) {
                // 删除七牛云服务器上无效的图片
                QiniuUtils.deleteFileFromQiniu(picName);
                // 删除redis集合中保存的图片名称
                jedisPool.getResource().srem(RedisConstant.ROUTE_IMG_ALL, picName);

                System.out.println("删除了无效图片-----------------------------------：" + picName);
            }
        }
    }

}