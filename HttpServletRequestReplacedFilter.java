package com.coinness.comment.filter;

import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author cph
 * springboot必须加@Component,springmvc不用
 */
@WebFilter(urlPatterns = {"/*","/**","//*","//**"},filterName = "httpServletRequestReplacedFilter")
@Order(1)
@Component
@Slf4j
public class HttpServletRequestReplacedFilter implements Filter {

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String contentType = httpRequest.getContentType();

        if (contentType != null &&
                contentType.contains("form")) {
            //如果是application/x-www-form-urlencoded, 参数值在request body中以 a=1&b=2&c=3...形式存在，
            //      若直接构造BodyReaderHttpServletRequestWrapper，在将流读取并存到copy字节数组里之后,
            //      httpRequest.getParameterMap()将返回空值！
            //      若运行一下 httpRequest.getParameterMap(), body中的流将为空! 所以两者是互斥的！
            httpRequest.getParameterMap();
        }


        MyHttpServletRequestWrapper requestWrapper = new MyHttpServletRequestWrapper(httpRequest);
        httpRequest = requestWrapper;
        if(httpRequest.getParameter("userId")==null){
            String userId = StringUtils.substring(httpRequest.getHeader("showId"), 1, -1);
            requestWrapper.addParameter("userId",userId);
        }
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        chain.doFilter(httpRequest, httpResponse);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
//        System.out.printf("fiter...");
    }
}

