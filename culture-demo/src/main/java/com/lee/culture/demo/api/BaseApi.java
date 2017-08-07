package com.lee.culture.demo.api;

import com.lee.culture.demo.ServiceException;
import com.lee.culture.demo.po.UserInfoEntity;
import com.lee.culture.demo.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: joe
 * @Date: 17-8-6 下午7:46.
 * @Description:
 */
public abstract class BaseApi {

    protected final Logger LOG = LogManager.getLogger(this.getClass());


    private static final ThreadLocal<Integer> loginUser = new ThreadLocal<>();

    @Autowired
    protected UserService userService;

    public static void setLoginUser(Integer userId) {
        loginUser.set(userId);
    }

    /**
     * 获取调用当前API的登录用户
     * @return
     */
    protected UserInfoEntity getLoginUser(){
        if (loginUser.get() == null) {
            throw new ServiceException("Authorization未设置");
        }

        UserInfoEntity user = userService.findById(loginUser.get());
        if (user == null) {
            throw new ServiceException("用户不存在");
        } else {
            return user;
        }
    }

    public static void removeLoginUser() {
        loginUser.remove();
    }
}
