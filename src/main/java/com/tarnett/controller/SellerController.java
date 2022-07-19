package com.tarnett.controller;

import com.tarnett.constant.MessageConstant;
import com.tarnett.entity.PageResult;
import com.tarnett.entity.QueryPageBean;
import com.tarnett.entity.Result;
import com.tarnett.pojo.Route;
import com.tarnett.pojo.Seller;
import com.tarnett.pojo.User;
import com.tarnett.service.SellerService;
import com.tarnett.service.impl.SellerServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Resource
    private SellerService sellerService;

    @RequestMapping("/queryAll")
    public Result queryALlSeller(){
        try {
            List<Seller> seller = sellerService.queryALlSeller();
            for (Seller seller1 : seller) {
                System.out.println(seller1.toString());
                System.out.println(seller1.getSname());
            }
            return new Result(true,"",seller);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SELLER_FAIL);
        }
    }

    @RequestMapping("/addSeller")
    public Result addRoute(@RequestBody Seller seller){
        try {
            sellerService.addSeller(seller);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_ROUTE_FAIL);
        }
        return new Result(true,MessageConstant.ADD_ROUTE_SUCCESS);
    }

    @RequestMapping("/queryPageManage")
    public PageResult<Seller> queryPage(@RequestBody QueryPageBean queryPageBean){
        return sellerService.queryPage(queryPageBean);
    }

    @RequestMapping("/queryOne")
    public Result queryOne(Integer sid){
        try {
            Seller seller = sellerService.querySellerById(sid);
            return new Result(true,MessageConstant.QUERY_USER_SUCCESS,seller);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_USER_FAIL);
        }
    }

    @RequestMapping("/modify")
    public Result modify(@RequestBody Seller seller){
        try {
            sellerService.modify(seller);
        } catch (Exception e) {
            e.printStackTrace();
            new Result(false,MessageConstant.UPDATE_USER_FAIL);
        }
        return new Result(true,MessageConstant.UPDATE_USER_SUCCESS);
    }

    @RequestMapping("/remove")
    public Result remove(Integer sid){
        try {
            sellerService.remove(sid);
            return new Result(true,MessageConstant.REMOVE_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.REMOVE_USER_FAIL);
        }
    }
}
