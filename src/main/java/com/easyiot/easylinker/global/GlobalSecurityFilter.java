package com.easyiot.easylinker.global;

import com.easyiot.easylinker.model.User;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 全局安全拦截器
 */
@Component
@WebFilter("/**")
public class GlobalSecurityFilter implements Filter {
    private final List<String> allowList = new ArrayList<>();

    public GlobalSecurityFilter() {
        allowList.add("/");
        allowList.add("/login");
        allowList.add("/data/api");


    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper((HttpServletResponse) servletResponse);
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        for (String s : allowList) {
            if (httpServletRequest.getRequestURI().startsWith(s) ||
                    httpServletRequest.getRequestURI().endsWith(".js") ||
                    httpServletRequest.getRequestURI().endsWith(".css")) {

                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                if (user != null) {
                    //System.out.println(user.getNickname() + " 已登录!");
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    //System.out.println("未登录" + "URL " + httpServletRequest.getRequestURI());

                    if (!httpServletRequest.getRequestURI().startsWith("/data/api")) {
                        wrapper.sendRedirect("/");
                    }


                }
            }
        }
    }
}