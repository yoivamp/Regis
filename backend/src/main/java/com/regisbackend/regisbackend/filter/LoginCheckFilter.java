package com.regisbackend.regisbackend.filter;

import com.alibaba.fastjson.JSON;
import com.regisbackend.regisbackend.common.MyBaseContext;
import com.regisbackend.regisbackend.common.PathLocation;
import com.regisbackend.regisbackend.common.Result;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 请求过滤器
 *
 * @author 喵vamp
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //转换
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1、获取本次请求的URI
        String requestUri = request.getRequestURI();
        //2、判断本次请求是否需要处理,如果不需要处理，则直接放行
        if (check(PathLocation.strings, requestUri)) {
            log.info("本次请求{}不需要处理", requestUri);
            filterChain.doFilter(request, response);
            return;
        }

        //3-1、判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户已登录，用户id为：{}", request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            MyBaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);
            return;
        }

        //3-2、判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("user") != null) {
            log.info("用户已登录，用户id为：{}", request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            MyBaseContext.setCurrentId(userId);

            filterChain.doFilter(request, response);
            return;
        }

        log.info("用户未登录");


        //4、如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
        return;
    }

    public boolean check(List<String> list, String requestUri) {
        for (String str : list) {
            if (PathLocation.PATH_MATCHER.match(str, requestUri)) {
                return true;
            }
        }
        return false;
    }
}
