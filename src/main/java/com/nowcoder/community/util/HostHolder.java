package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

/**
 *  持有用户信息，用于代替session对象
 */
@Component
public class HostHolder {
    /**
     *     他是线程隔离的, 源码中能看，调用的是Thread.currentThread()获取当前线程
      */
    private ThreadLocal<User> users=new ThreadLocal<>();

    public void setUser(User user){
        users.set(user);
    }

    public User getUser(){
        return users.get();
    }

    public void clear(){
        users.remove();
    }
}
