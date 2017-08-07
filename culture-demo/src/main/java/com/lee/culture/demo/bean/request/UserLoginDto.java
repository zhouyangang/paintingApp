package com.lee.culture.demo.bean.request;

import javax.validation.constraints.NotNull;

/**
 * @Author: joe
 * @Date: 17-8-6 下午6:05.
 * @Description:
 */
public class UserLoginDto {

    @NotNull(message = "手机号不能为空")
    private String phone;

    @NotNull(message = "密码不能为空")
    private String password;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
