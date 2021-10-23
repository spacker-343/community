package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * 持有用户信息，用于代替session对象
 */
@Component
public class HostHolder {
    /**
     * 他是线程隔离的, 源码中能看，调用的是Thread.currentThread()获取当前线程
     */
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    /**
     * ThreadLocal 只能存放一个entry，即key为当前线程，value为你存放的值
     * 通过entry.value来获取值
     * ThreadLocalMap.Entry e = map.getEntry(this);
     * e.value
     */
    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
