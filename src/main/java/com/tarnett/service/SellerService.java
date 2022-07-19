package com.tarnett.service;


import com.tarnett.entity.PageResult;
import com.tarnett.entity.QueryPageBean;
import com.tarnett.pojo.Route;
import com.tarnett.pojo.Seller;
import com.tarnett.pojo.User;

import java.util.List;

public interface SellerService {

    List<Seller> queryALlSeller();

    PageResult<Seller> queryPage(QueryPageBean queryPageBean);

    Seller querySellerById(Integer sid);

    int addSeller(Seller seller);

    void modify(Seller seller);

    public void remove(Integer sid);
}
