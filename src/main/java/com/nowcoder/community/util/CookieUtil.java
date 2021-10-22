package com.nowcoder.community.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {

    /**
     *
     * @param request 从请求中获取cookie
     * @param name 想要获取的cookie的名称
     * @return 返回对应cookie的值
     */
    public static String getValue(HttpServletRequest request, String name){
        if(request==null||name==null){
            throw new IllegalArgumentException("参数为空!");
        }

        Cookie[] cookies=request.getCookies();
        if(cookies!=null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
