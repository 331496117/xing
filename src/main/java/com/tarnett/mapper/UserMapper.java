package com.tarnett.mapper;

import com.github.pagehelper.Page;
import com.tarnett.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    public void insert(User user);

    public User selectByUsername(String username);
    
    public User selectByCode(String username);

    public void updateStatus(User user);

    User selectByUsernameAndPassword(User user);

    Page<User> selectByCondition(@Param("queryString")String queryString);

    User selectUserById(@Param("uid")Integer uid);

    void updateUser(User user);

    int delete(@Param("uid")Integer uid);

    void updateStatusByAuto(@Param("uid")Integer uid,@Param("status")String status);
}