package com.lee.culture.demo.api;

import com.lee.culture.demo.bean.request.UserProfileDto;
import com.lee.culture.demo.po.UserInfoEntity;
import com.lee.culture.demo.po.WorkInfoEntity;
import com.lee.culture.demo.service.UserService;
import io.swagger.annotations.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.Collections;

/**
 * @Author: joe
 * @Date: 17-7-29 下午8:03.
 * @Description:
 */
@Api("/artists")
@SwaggerDefinition(info = @Info(contact = @Contact(name = "Joe", email = "174492779@qq.com"), title = "The user api for user management", version = "1.0.0"))
@Validated
@RestController
@RequestMapping("/artists")
public class UserApi extends BaseApi {


    @PutMapping(value = "/profile")
    public UserInfoEntity updateProfile(@RequestBody @NotNull UserProfileDto dto) {

        return userService.updateProfile(getLoginUser(), dto);

    }

    @GetMapping(value = "/profile")
    public UserInfoEntity getProfile() {
        return getLoginUser();
    }

    @PostMapping(value = "/avastar/upload")
    public String uploadAvastar(@RequestParam("picture") MultipartFile picture) {
        return userService.uploadAvastar(getLoginUser(), picture);
    }
}
