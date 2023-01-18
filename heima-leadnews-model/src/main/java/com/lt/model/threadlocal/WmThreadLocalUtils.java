package com.lt.model.threadlocal;

import com.lt.model.wemedia.pojo.WmUser;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/1/18 22:39
 */
public class WmThreadLocalUtils {
    private static final ThreadLocal<WmUser> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 设置当前线程中的用户
     */
    public static void setUser(WmUser user) {
        THREAD_LOCAL.set(user);
    }

    /**
     * 获取线程中的用户
     */
    public static WmUser getUser() {
        return THREAD_LOCAL.get();
    }

    /**
     * 清空线程中的用户信息
     */
    public static void clear() {
        THREAD_LOCAL.remove();
    }
}
