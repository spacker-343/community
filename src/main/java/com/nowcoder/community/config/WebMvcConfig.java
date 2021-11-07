package com.nowcoder.community.config;

import com.nowcoder.community.controller.interceptor.AlphaInterceptor;
import com.nowcoder.community.controller.interceptor.LoginRequiredInterceptor;
import com.nowcoder.community.controller.interceptor.LoginTicketInterceptor;
import com.nowcoder.community.controller.interceptor.MessageInterceptor;
import com.nowcoder.community.entity.LoginTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sun.net.www.content.image.jpeg;
import sun.net.www.content.image.png;

// 参考接口名字命的名
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private AlphaInterceptor alphaInterceptor;

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

//    @Autowired
//    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(alphaInterceptor)
                // 首先要排除掉对静态资源的拦截，排除访问的路径
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg")
                // 想添加的拦截路径
                .addPathPatterns("/register", "/login");

        // 不加addPathPatterns就是全部
        registry.addInterceptor(loginTicketInterceptor)
                // 首先要排除掉对静态资源的拦截，排除访问的路径
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        // 改用springsecurity
//        // 登录
//        registry.addInterceptor(loginRequiredInterceptor)
//                // 首先要排除掉对静态资源的拦截，排除访问的路径
//                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png ", "/**/*.jpg", "/**/*.jpeg ");

        registry.addInterceptor(messageInterceptor)
                // 首先要排除掉对静态资源的拦截，排除访问的路径
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
    }
}
