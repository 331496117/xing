package com.tarnett.mapper;

import com.tarnett.pojo.Favorite;
import org.apache.ibatis.annotations.Param;

public interface FavoriteMapper {
    Favorite selectByRIdAndUid(@Param("rid") Integer rid, @Param("uid") Integer uid);

    void insert(Favorite favorite);

    void deleteByRid(@Param("rid") Integer rid, @Param("uid") Integer uid);
}
