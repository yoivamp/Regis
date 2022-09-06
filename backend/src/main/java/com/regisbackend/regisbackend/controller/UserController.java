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

import java.util.Map;

/**
 * @author 喵vamp
 * @Description 移动端用户管理
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
    public Result<String> sendMsg(@RequestBody User user) {
        return userService.sendMsg(user);
    }

    /**
     * 移动端用户登录
     *
     * @param map 用户信息
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody Map map) {
        return userService.login(map);
    }


    /**
     * 退出登录
     *
     * @return 退出登录
     */
    @PostMapping("/loginout")
    public Result<String> loginout() {
        return Result.success("退出成功");
    }
}
