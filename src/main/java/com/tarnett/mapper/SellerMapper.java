package com.tarnett.mapper;

import com.github.pagehelper.Page;
import com.tarnett.pojo.Route;
import com.tarnett.pojo.Seller;
import com.tarnett.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SellerMapper {

    List<Seller> queryAllSeller();

    Page<Seller> selectByCondition(@Param("queryString") String queryString);

    int insertSeller(Seller seller);

    Seller selectSellerById(@Param("sid")Integer sid);

    void updateSeller(Seller seller);

    public void deleteSeller(@Param("sid")Integer sid);

}
