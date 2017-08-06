package com.lee.culture.demo.service.impl;

import com.lee.culture.demo.dao.UserRepository;
import com.lee.culture.demo.po.UserInfoEntity;
import com.lee.culture.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: joe
 * @Date: 17-8-5 下午10:05.
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userDao;

    @Override
    public UserInfoEntity findByPhone(String phone) {
        return userDao.findByPhone(phone);
    }
}
