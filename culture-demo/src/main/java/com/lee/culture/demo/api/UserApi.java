package com.lee.culture.demo.api;

import com.lee.culture.demo.po.UserInfoEntity;
import com.lee.culture.demo.po.WorkInfoEntity;
import com.lee.culture.demo.service.UserService;
import io.swagger.annotations.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Collections;

/**
 * @Author: joe
 * @Date: 17-7-29 下午8:03.
 * @Description:
 */
@Api("/user")
@SwaggerDefinition(info = @Info(contact = @Contact(name = "Joe", email = "174492779@qq.com"), title = "The user api for user management", version = "1.0.0"))
@Validated
@RestController
@RequestMapping("/user")
public class UserApi {

    private static final Logger LOG = LogManager.getLogger(UserApi.class);

    @Autowired
    private UserService userService;


    @PostMapping(value = "/login")
    public UserInfoEntity login(@RequestBody @NotNull UserInfoEntity user) {

        return userService.findByPhone(user.getPhone());
    }
}
