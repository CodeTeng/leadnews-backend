package com.lt.model.threadlocal;

import com.lt.model.user.pojo.ApUser;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/26 17:51
 */
public class AppThreadLocalUtils {
    private final static ThreadLocal<ApUser> userThreadLocal = new ThreadLocal<>();

    /**
     * 设置当前线程中的用户
     */
    public static void setUser(ApUser user) {
        userThreadLocal.set(user);
    }

    /**
     * 获取线程中的用户
     */
    public static ApUser getUser() {
        return userThreadLocal.get();
    }

    /**
     * 清空线程中的用户信息
     */
    public static void clear() {
        userThreadLocal.remove();
    }
}
