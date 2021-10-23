package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 这个拦截器要注册在LoginTicketInterceptor之后，WebMvcConfig中注册
 * 好处，我希望拦截哪个方法，就在它上面加LoginRequired注解就可以了
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断拦截到的是否是Controller中的方法，因为有可能拦截到静态资源
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 因为HandlerMethod封装有关由方法和bean组成的处理程序方法的信息

            // 老师写的方法
//            Method method = handlerMethod.getMethod();
//            LoginRequired loginRequired=method.getAnnotation(LoginRequired.class);
//            if(loginRequired!=null&&hostHolder.getUser()==null){
//                // 当方法上有@LoginRequired，且用户未登录，就重定向回登录页面
//                response.sendRedirect(request.getContextPath()+"/login");
//                // 不用往后执行了
//                return false;
//            }

            // 我看了源码之后觉得可以这样写
            // 如果存在方法注解应该返回true，不存在
            if (handlerMethod.hasMethodAnnotation(LoginRequired.class) && hostHolder.getUser() == null) {
                // 当方法上有@LoginRequired，且用户未登录，就重定向回登录页面
                response.sendRedirect(request.getContextPath() + "/login");
                // 不用往后执行了
                return false;
            }
        }
        return true;
    }
}
