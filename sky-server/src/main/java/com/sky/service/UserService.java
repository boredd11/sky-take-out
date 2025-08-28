package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    //微信用户的登录
    User wxLogin(UserLoginDTO userLoginDTO);
}
