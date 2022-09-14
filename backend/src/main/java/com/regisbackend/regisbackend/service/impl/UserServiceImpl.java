package com.regisbackend.regisbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.dao.UserMapper;
import com.regisbackend.regisbackend.pojo.User;
import com.regisbackend.regisbackend.service.UserService;
import com.regisbackend.regisbackend.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.regisbackend.regisbackend.utils.RedisKeys.*;

/**
 * @author 喵vamp
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 发送手机短信验证码
     *
     * @param user 用户信息
     * @return 验证码
     */
    @Override
    public Result<String> sendMsg(User user) {
        //获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            System.out.println("code is----------->" + code);
            log.info("code={}", code);
            //调用阿里云提供的短信服务API完成发送短信
            //SMSUtils.sendMessage("瑞吉外卖","",phone,code);

            //将生成的验证码缓存到Redis中，并且设置有效期
            stringRedisTemplate.opsForValue()
                    .set(USER_LOGIN_CODE_KEY + phone, code, USER_LOGIN_CODE_TTL, TimeUnit.MINUTES);
            return Result.success("手机验证码短信发送成功");
        }
        return Result.error("短信发送失败");
    }

    /**
     * 移动端用户登录
     *
     * @param map 用户信息
     * @return 登录结果
     */
    @Override
    public Result<User> login(Map map) {
        log.info(map.toString());
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从Redis中获取缓存的验证码
        String codeInSession = stringRedisTemplate.opsForValue().get(USER_LOGIN_CODE_KEY + phone);
        //进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）
        if (codeInSession != null && codeInSession.equals(code)) {
            //如果能够比对成功，说明登录成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            //查询是否存在该手机号的用户
            User user = this.getOne(queryWrapper);
            if (user == null) {
                //当前手机号对应的用户是否为新用户，自动完成注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                this.save(user);
            }
            //如果用户登录成功，删除Redis中缓存的验证码
            stringRedisTemplate.delete(USER_LOGIN_CODE_KEY + phone);
            //登录成功存储一份用户信息到redis
            stringRedisTemplate.opsForValue().set(USER_LOGINED_KEY, user.getPhone());
            //设置用户信息有效期
            stringRedisTemplate.expire(USER_LOGINED_KEY, USER_LOGINED_TTL, TimeUnit.MINUTES);
            return Result.success(user);
        }
        return Result.error("登录失败");
    }
}
