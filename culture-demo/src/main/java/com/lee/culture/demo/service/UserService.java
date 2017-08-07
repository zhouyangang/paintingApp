package com.lee.culture.demo.service;

import com.lee.culture.demo.bean.request.UserLoginDto;
import com.lee.culture.demo.bean.request.UserProfileDto;
import com.lee.culture.demo.po.UserInfoEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author: joe
 * @Date: 17-8-5 下午10:04.
 * @Description:
 */
public interface UserService {

    UserInfoEntity findByPhone(String phone);

    UserInfoEntity signUp(UserLoginDto dto);

    UserInfoEntity findById(Integer integer);

    UserInfoEntity updateProfile(UserInfoEntity user, UserProfileDto dto);

    String uploadAvastar(UserInfoEntity loginUser, MultipartFile picture) throws IOException;
}
