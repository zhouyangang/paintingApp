package com.lee.culture.demo.service;

import com.lee.culture.demo.po.UserInfoEntity;

/**
 * @Author: joe
 * @Date: 17-8-5 下午10:04.
 * @Description:
 */
public interface UserService {

    UserInfoEntity findByPhone(String phone);
}
