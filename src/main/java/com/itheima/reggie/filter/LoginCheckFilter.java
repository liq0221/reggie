package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: test
 * @Create by: 草滩彭于晏
 * @Date:2021/10/31 11:03
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //拿到请求数据中的uri
        String requestURI = request.getRequestURI();

        //log.info("拦截到请求：{}",requestURI);

        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/backend/**",
                "/employee/login",
                "/front/**",
                "/user/sendMsg",
                "/user/login"

        };

        //判断是否放行
        boolean check = check(urls, requestURI);

        //不需要处理,放行
        if(check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        //需要处理,进行登录判断(后台)
        if(request.getSession().getAttribute("employee") != null){
            Long empID = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empID);
            //登录了 放行
            filterChain.doFilter(request, response);
            return;
        }

        //需要处理,进行登录判断(前台)
        if(request.getSession().getAttribute("user") != null){
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            //登录了 放行
            filterChain.doFilter(request, response);
            return;
        }

        //没有登录
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    /**
     * 路径匹配 检查本次是否放行
     *
     * @param urls
     * @param requestUri
     * @return
     */
    public boolean check(String[] urls, String requestUri) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestUri);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
