package com.lee.culture.demo.api;

import com.lee.culture.demo.ServiceException;
import com.lee.culture.demo.bean.request.UserLoginDto;
import com.lee.culture.demo.po.UserInfoEntity;
import com.lee.culture.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @Author: joe
 * @Date: 17-8-6 下午6:03.
 * @Description:
 */
@RestController
@Validated
@Api("/")
@SwaggerDefinition(info = @Info(contact = @Contact(name = "Joe", email = "174492779@qq.com"), title = "The system api for user login", version = "1.0.0"))
public class SystemApi extends BaseApi{


    @PostMapping("/sign-up")
    public UserInfoEntity signUp(@RequestBody @NotNull @Valid UserLoginDto dto) {

        return userService.signUp(dto);

    }

    @PostMapping("/sign-in")
    public UserInfoEntity login(@RequestBody @NotNull @Valid UserLoginDto dto) {
        UserInfoEntity user = userService.findByPhone(dto.getPhone());
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        if (dto.getPassword().equals(user.getPwd())) {
            return user;
        } else {
            throw new ServiceException("密码错误");
        }

    }

    @PostMapping("/sign-out")
    public void logout() {
        LOG.info(getLoginUser().getId() + " log out");
    }
}
