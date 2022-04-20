package com.regisbackend.regisbackend.controller;

import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.pojo.User;
import com.regisbackend.regisbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author 喵vamp
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 发送手机短信验证码
     *
     * @param user 用户信息
     * @return 验证码
     */
    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpSession session) {
        return userService.sendMsg(user, session);
    }

    /**
     * 移动端用户登录
     *
     * @param map     用户信息
     * @param session 存储手机号
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody Map map, HttpSession session) {
        return userService.login(map, session);
    }


    /**
     * 退出登录
     *
     * @param request 删除存储用户id
     * @return 退出登录
     */
    @PostMapping("/loginout")
    public Result<String> loginout(HttpServletRequest request) {
        //清理Session中保存的当前登录用户的id
        request.getSession().removeAttribute("user");
        return Result.success("退出成功");
    }
}
