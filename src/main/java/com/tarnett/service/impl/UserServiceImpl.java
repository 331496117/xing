package com.tarnett.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tarnett.entity.PageResult;
import com.tarnett.entity.QueryPageBean;
import com.tarnett.mapper.UserMapper;
import com.tarnett.pojo.Route;
import com.tarnett.pojo.User;
import com.tarnett.service.UserService;
import com.tarnett.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

// 声明该类是一个业务逻辑层的实现类
@Service
@Transactional
public class UserServiceImpl implements UserService {

    // 自动注入
    @Autowired
    private UserMapper userMapper;
    /**
     * 用户注册
     */
    public void regist(User user) {
        // 1. 校验用户名唯一性 TODO
        User checkUser = userMapper.selectByUsername(user.getUsername());
        if(checkUser!=null){
            throw new RuntimeException();
        }
        // 2. 需要对密码进行加密
        try {
            String newPassword=Md5Util.encodeByMd5(user.getPassword());
            user.setPassword(newPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 保存用户之前需要生成一个UUID激活码，并且设置到用户里面
        String code = UUID.randomUUID().toString();
        user.setCode(code);
        // 设置用户激活状态，初始化的情况下，用户激活状态为N
        //user.setStatus("N");
        // 3. 调用数据访问层的方法保存数据
        userMapper.insert(user);
        // 4. 给用户发送激活邮件
//        String content="恭喜您注册成功，点击立即激活，激活账号 <a href='http://localhost:8080/user/active.do?code="+ code +"'>【融云旅游网】</a>";
//        System.out.println(content);
       // MailUtils.sendMail(user.getEmail(),content,"【融云旅游网】激活邮件");
    }

    /**
     * 根据用户名查找用户
     */
    public User checkUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    /**
     * 修改用户的激活码
     * @param code
     * @return
     */
    public User active(String code) {
        User user = userMapper.selectByCode(code);
        // 如果通过激活码不能够找到用户，则表明激活码错误 返回一个 Null
       if(user==null){
           return null;
       }

       // 如果还为激活，则通过 updateStatus 激活
       if(user.getStatus().equals("N")){
           userMapper.updateStatus(user);
       }
       return user;
    }

    @Override
    public User login(User user){
        // 1.对密码进行加密
        try {
            String newPassword = Md5Util.encodeByMd5(user.getPassword());
            user.setPassword(newPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. 调用数据访问层查询数据
        return userMapper.selectByUsernameAndPassword(user);
    }

    @Override
    public PageResult<User> queryPage(QueryPageBean queryPageBean) {
        // 设置 mybatis 的分页插件
        // 设置分页参数
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<User> users = userMapper.selectByCondition(queryPageBean.getQueryString());
        return new PageResult<>(users.getTotal(),users.getResult());
    }

    @Override
    public User queryUserById(Integer uid) {
        return userMapper.selectUserById(uid);
    }

    @Override
    public void modify(User user) {
        // 2. 需要对密码进行加密
        try {
            String newPassword=Md5Util.encodeByMd5(user.getPassword());
            user.setPassword(newPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        userMapper.updateUser(user);
    }

    @Override
    public void remove(Integer uid) {
        userMapper.delete(uid);
    }

    @Override
    public void updateStatusByAuto(Integer uid) {
        String status = userMapper.selectUserById(uid).getStatus();
        status=status.equals("Y")?"N":"Y";
        userMapper.updateStatusByAuto(uid,status);
    }
}