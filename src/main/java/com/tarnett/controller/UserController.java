package com.tarnett.controller;

import com.tarnett.constant.MessageConstant;
import com.tarnett.entity.PageResult;
import com.tarnett.entity.QueryPageBean;
import com.tarnett.entity.Result;
import com.tarnett.pojo.User;
import com.tarnett.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

// 1. 声明该类是一个控制器
// 2. 该控制器响应的都是数据
@RestController
@RequestMapping("/user")
public class UserController {

    // 自动注入
    @Autowired
    private UserService userService;

    @RequestMapping("/checkUsername")
    public Result checkUsername(String username) {
        // 对数据进行规则校验
        if(username==null||username.length()==0){
            return new Result(false,MessageConstant.REGIST_USERNAME_NO_FAIL);
        }
        if(username.length()<3||username.length()>8){
            return new Result(false,MessageConstant.REGIST_USERNAME_FAIL);
        }

        User user = userService.checkUsername(username);
        if(user != null) {// 如果用户名不为空，则说明该用户名已经被注册使用了
            return new Result(false, MessageConstant.REGIST_USERNAME_EXSIT);
        }

        return new Result(true, "");
    }

    @RequestMapping("/registe")
    public Result regist(User user, String check, HttpSession session, User user1) {
        System.out.println("请求进来控制了");

        if(user.getUsername()==null||user.getUsername().length()==0){
            return new Result(false,MessageConstant.REGIST_USERNAME_NO_FAIL);
        }
        if(user.getUsername().length()<3||user.getUsername().length()>8){
            return new Result(false,MessageConstant.REGIST_USERNAME_FAIL);
        }
        // 其他校验自己写
        // 2. 校验验证码是否正确 TODO
        if(check==null||check.length()==0){
            return new Result(false,"请输入验证密码");
        }
        if(check.length()!=4){
            return new Result(false,"请输入正确的验证码!!");
        }

        // 2.1 获取用户提交的验证码 ：在方法形参里面添加String check
        // 2.2 获取session中保存的验证码
        String session_code = (String) session.getAttribute(MessageConstant.CHECKCODE_SERVER);

        // 因为验证码是一次性的，使用完后需要清除验证码
        session.removeAttribute(MessageConstant.CHECKCODE_SERVER);
        if(session_code==null||!session_code.equalsIgnoreCase(check)){
            return new Result(false,MessageConstant.REGIST_CHECK_FAIL);
        }
        // 3. 调用业务逻辑层的方法去保存用户的数据
        try {
            userService.regist(user1);
        }catch (RuntimeException e){
            return new Result(false,MessageConstant.REGIST_USERNAME_EXSIT);
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.REGIST_USER_FAIL);
        }
        return new Result(true, MessageConstant.REGIST_USER_SUCCESS);
    }

    @RequestMapping("/active")
    public void active(String code, HttpServletResponse response){
        try {
            // 1. 获取请求参数 通过在方法的形参中添加一个code参数，可以获取激活码
            User user=userService.active(code);

            if(user==null){
                response.sendRedirect("/pages/active_fail.html");
            }else {
                // 通过激活码能够查询到用户
                if(user.getStatus().equals("Y")){
                    response.sendRedirect("/pages/active.html");
                }else {
                    // 如果用户没有激活，则表示用户激活成功
                    response.sendRedirect("/pages/active_ok.html");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/login")
    public Result login(User user, String check, HttpSession session, HttpServletRequest request,HttpServletResponse response){
        // 0. 定义一个局部变量，用来接受showCheck这个cookie
        Cookie showCheckCookie=null;
        // 0. 定义一个标志为，用来判断是否需要校验码 false: 不需要验证码  true:需要校验验证码
        boolean flag=false;
        // 1. 获取所有的cookie
        Cookie[] cookies = request.getCookies();
        // 2. 需要对Cookie进行遍历，查找是否存在showCheck名字的cookie
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("showCheck")){
                showCheckCookie=cookie;
                // 获取该cookie中的值，并且转换为int类型
                int count=Integer.parseInt(showCheckCookie.getValue());
                if(count>=3){
                    // 需要检验验证码
                    flag=true;
                    break;
                }
            }
        }

        // 在不存在showCheck这个cookie
        // 存在showCheck这个cookie,但是其值小于3

        // 1. 对数据进行规则校验 TODO
        // 2. 对验证码进行校验
        // 2.1 获取session中保存的验证码
        if(flag){
            String session_code =(String) session.getAttribute(MessageConstant.CHECKCODE_SERVER);
            // 2.2 清除session中保存的验证码数据
            session.removeAttribute(MessageConstant.CHECKCODE_SERVER);
            // 2.3 对验证码的内容进行比较
            if(session_code==null||!session_code.equalsIgnoreCase(check)){
                return new Result(false,MessageConstant.REGIST_CHECK_FAIL);
            }
        }

        // 3. 调用业务逻辑层登陆方法
        User loginUser=userService.login(user);
        // 4. 对返回结果进行判断
        if(loginUser==null){
            if(showCheckCookie==null){
                // 第一次账号或密码错误
                showCheckCookie=new Cookie("showCheck","1");
            }else {
                // 不是第一次账号或者密码错误
                // 获取原来的值
                int count = Integer.parseInt(showCheckCookie.getValue());
                // 在原来的基础上进行加 1
                count=count+1;
                // 把加 1之后的值设置回cookie对象
                showCheckCookie.setValue(count+"");
            }
            // 需要把cookie发送给浏览器
            response.addCookie(showCheckCookie);

            // 说明用户名或者密码错误，登陆失败
            return new Result(false,"用户名或密码错误");
        }
        // 5. 判断用户是否已经激活
        if(loginUser.getStatus().equals("N")){
            // 说明用户没有激活
            return new Result(false,"该账户尚未激活,请立即激活");
        }
        // 表示用户登陆成功
        // 用户登陆成功之后需要将用户信息保存到session
        session.setAttribute("loginUser",loginUser);
        // 用户登陆成功之后需要删除showCheck这个cookie
        showCheckCookie=new Cookie("showCheck","0");
        // 设置其最大的存活时间，单位为秒
        showCheckCookie.setMaxAge(0);
        // 需要把cookie发送给浏览器
        response.addCookie(showCheckCookie);
        return new Result(true,"");
    }

    @RequestMapping("/queryUserInfo")
    public Result queryUserInfo(HttpSession session){
        // 1.从session中查询登陆用户的信息
        User user = (User) session.getAttribute("loginUser");
        // 2. 对session中获取的user对象进行判断
        if(user==null){
            //判断是否为空，如果
            return new Result(false,"");
        }
        return new Result(true,"",user);
    }

    @RequestMapping("/loginOut")
    public Result loginOut(HttpSession session){
        // 需要删除session中保存的用户数据
        session.removeAttribute("loginUser");
        // 是session失效
        session.invalidate();
        return new Result(true,"");
    }

    @RequestMapping("/showCheck")
    public Result showCheck(HttpServletRequest request){
        // 1. 需要获取所有的cookie
        Cookie[] cookies = request.getCookies();
        // 2. 判断是否存在名字为showCheck的cookie,并且其值是否大于3
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("showCheck")){
                // 说明存在名字为showCheck的cookie对象
                int count = Integer.parseInt(cookie.getValue());
                if(count>=3){
                    // 需要显示验证码
                    return new Result(true,"");
                }
            }
        }
        return new Result(false,"");
    }

    @RequestMapping("/queryPageManage")
    public PageResult<User> queryPage(@RequestBody QueryPageBean queryPageBean){
        return userService.queryPage(queryPageBean);
    }

    @RequestMapping("/queryOne")
    public Result queryOne(Integer uid){
        try {
            User user = userService.queryUserById(uid);
            return new Result(true,MessageConstant.QUERY_USER_SUCCESS,user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_USER_FAIL);
        }
    }

    @RequestMapping("/modify")
    public Result modify(@RequestBody User user){
        try {
            userService.modify(user);
        } catch (Exception e) {
            e.printStackTrace();
            new Result(false,MessageConstant.UPDATE_USER_FAIL);
        }
        return new Result(true,MessageConstant.UPDATE_USER_SUCCESS);
    }

    @RequestMapping("/remove")
    public Result remove(Integer uid){
        try {
            userService.remove(uid);
            return new Result(true,MessageConstant.REMOVE_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.REMOVE_USER_FAIL);
        }
    }

    @RequestMapping("/updateStatus")
    public Result updateStatus(Integer uid){
        try {
            userService.updateStatusByAuto(uid);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.UPDATE_USER_STATUS_FAIL);
        }
        return new Result(true,MessageConstant.UPDATE_USER_STATUS_SUCCESS);
    }

    @RequestMapping("/addUser")
    public Result addUser(@RequestBody User user){
        try {
            userService.regist(user);
        }catch (RuntimeException e){
            return new Result(false,MessageConstant.REGIST_USERNAME_EXSIT);
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.REGIST_USER_FAIL);
        }
        return new Result(true, MessageConstant.REGIST_USER_SUCCESS);
    }
}