package com.tarnett.service;

import com.tarnett.pojo.Favorite;

public interface FavoriteService {
    Favorite queryFavorite(Integer rid, Integer uid);

    void addFavorite(Integer rid, Integer uid);

    void removeFavorite(Integer rid, Integer uid);
}
