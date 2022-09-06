package com.regisbackend.regisbackend.filter;

import com.alibaba.fastjson.JSON;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.regisbackend.regisbackend.common.PathLocation;
import com.regisbackend.regisbackend.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.regisbackend.regisbackend.utils.RedisKeys.*;

/**
 * 请求过滤器
 *
 * @author 喵vamp
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (isEmployeeLogin(EMPLOYEE_LOGIN_KEY)) {
            //刷新员工登录信息有效期
            stringRedisTemplate.expire(EMPLOYEE_LOGIN_KEY, EMPLOYEE_LOGIN_TTL, TimeUnit.MINUTES);
        }
        if (isUserLogin(USER_LOGINED_KEY)) {
            //刷新用户登录信息有效期
            stringRedisTemplate.expire(USER_LOGINED_KEY, USER_LOGINED_TTL, TimeUnit.MINUTES);
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //转换
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取本次请求的URI
        String requestUri = request.getRequestURI();
        //判断本次请求是否需要处理,如果不需要处理，则直接放行
        if (check(PathLocation.strings, requestUri)) {
            log.info("本次请求{}不需要处理", requestUri);
            filterChain.doFilter(request, response);
            return;
        }
        //判断员工登录状态，如果已登录，则直接放行
        if (isEmployeeLogin(EMPLOYEE_LOGIN_KEY)) {
            filterChain.doFilter(request, response);
        }
        //判断用户登录状态，如果已登录，则直接放行
        else if (isUserLogin(USER_LOGINED_KEY)) {
            filterChain.doFilter(request, response);
        } else {
            //未登录，通过输出流方式向客户端页面响应数据
            log.info("用户未登录");
            response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
        }
    }

    /**
     * 检查请求路径路径是否为必要路径
     *
     * @param list       必要路径列表
     * @param requestUri 请求路径
     * @return 检查结果
     */
    public boolean check(List<String> list, String requestUri) {
        for (String str : list) {
            if (PathLocation.PATH_MATCHER.match(str, requestUri)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证登录
     *
     * @param key 员工登录的key
     * @return 登录结果
     */
    public boolean isEmployeeLogin(String key) {
        String str = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(str)) {
            log.info("员工登录，员工信息为 {}", str);
            return true;
        }
        return false;
    }

    public boolean isUserLogin(String key) {
        String str = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(str)) {
            log.info("用户登录，用户信息为 {}", str);
            return true;
        }
        return false;
    }
}
