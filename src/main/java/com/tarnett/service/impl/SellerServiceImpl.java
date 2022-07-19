package com.tarnett.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tarnett.constant.RedisConstant;
import com.tarnett.entity.PageResult;
import com.tarnett.entity.QueryPageBean;
import com.tarnett.mapper.SellerMapper;
import com.tarnett.pojo.Route;
import com.tarnett.pojo.Seller;
import com.tarnett.pojo.User;
import com.tarnett.service.SellerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {

    @Resource
    private SellerMapper sellerMapper;

    @Override
    public List<Seller> queryALlSeller() {
        return sellerMapper.queryAllSeller();
    }

    @Override
    public PageResult<Seller> queryPage(QueryPageBean queryPageBean) {
        // 设置 mybatis 的分页插件
        // 设置分页参数
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Seller> pages = sellerMapper.selectByCondition(queryPageBean.getQueryString());
        return new PageResult<>(pages.getTotal(),pages.getResult());
    }

    @Override
    public Seller querySellerById(Integer sid) {
        return sellerMapper.selectSellerById(sid);
    }

    @Override
    public int addSeller(Seller seller) {
        int rows = sellerMapper.insertSeller(seller);
        // 将保存到数据库中的图片名称保存到 redis 中
        return rows;
    }

    @Override
    public void modify(Seller seller) {
        sellerMapper.updateSeller(seller);
    }

    @Override
    public void remove(Integer sid) {
        sellerMapper.deleteSeller(sid);
    }
}
