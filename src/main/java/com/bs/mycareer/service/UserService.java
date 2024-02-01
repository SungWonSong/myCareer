package com.bs.mycareer.service;

import com.bs.mycareer.dto.RegisterRequest;
import com.bs.mycareer.entity.User;

public interface UserService {
    void registerProcess(RegisterRequest userRegisterDto);

}
