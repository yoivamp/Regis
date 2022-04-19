package com.regisbackend.regisbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.pojo.User;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author 喵vamp
 */
public interface UserService extends IService<User> {

    /**
     * 发送验证码
     * @param user 用户手机号
     * @param session 存储手机号
     * @return 验证信息
     */
    Result<String> sendMsg(User user, HttpSession session);

    /**
     * 移动端用户登录
     *
     * @param map     用户信息
     * @param session 存储手机号
     * @return 登录结果
     */
    Result<User> login(Map map, HttpSession session);
}
