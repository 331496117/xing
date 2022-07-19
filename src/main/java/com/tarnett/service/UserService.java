package com.tarnett.service;
import com.tarnett.entity.PageResult;

import com.tarnett.entity.QueryPageBean;

import com.tarnett.pojo.User;

public interface UserService {

	public void regist(User user);

	public User checkUsername(String username);

	public User active(String code);

	public User login(User user);

	PageResult<User> queryPage(QueryPageBean queryPageBean);

	User queryUserById(Integer uid);

	void modify(User user);

	public void remove(Integer uid);

	public void updateStatusByAuto(Integer uid);
}