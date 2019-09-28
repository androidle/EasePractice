package com.leapp.yangle.module.impl;

import com.google.gson.Gson;
import com.leapp.yangle.arouter.annotations.ARouter;
import com.leapp.yangle.common.app.IUser;
import com.leapp.yangle.module.model.User;

@ARouter(path = "/app/getUser")
public class AppUserImpl implements IUser {

    @Override
    public String getUser() {
        User user = new User();
        user.setAge(31);
        user.setIdCard("610431");
        user.setName("张三");
        return new Gson().toJson(user);
    }
}
