package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CookieUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 先要通过cookie获取ticket，不过因为这是重写方法，所以我们不能通过cookievalue获取cookie
        // 所以从request取cookie，然后稍微封装一下
        String ticket= CookieUtil.getValue(request, "ticket");

        // 判断客户端是否传过来了cookie
        if(ticket!=null){
            // 通过ticket查询数据库中对应凭证
            LoginTicket loginTicket= userService.findLoginTicket(ticket);
            // 检查凭证是否有效
            // 不为空 未失效 过期时间在当前日期之后
            if(loginTicket!=null&&loginTicket.getStatus()==0&&loginTicket.getExpired().after(new Date())){
                // 查询凭证对应的用户信息
                User user = userService.findUserById(loginTicket.getUserId());
                // 信息会在模板或服务器中要用
                // 在本次请求中持有用户
                // 线程隔离用ThreadLocal
                hostHolder.setUser(user);
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user=hostHolder.getUser();
        if(user!=null && modelAndView!=null){
            modelAndView.addObject("loginUser", user);
        }
    }

    /**
     *     模板和model拼接并返回给DisPatcherServelet后调用
      */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除掉线程里的User对象
        hostHolder.clear();
    }
}
