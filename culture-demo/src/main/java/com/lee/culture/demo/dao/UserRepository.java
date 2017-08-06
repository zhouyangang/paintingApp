package com.lee.culture.demo.dao;

import com.lee.culture.demo.po.UserInfoEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @Author: joe
 * @Date: 17-8-5 下午9:55.
 * @Description:
 */
public interface UserRepository extends CrudRepository<UserInfoEntity, Integer> {

    UserInfoEntity findByPhone(String phone);
}
