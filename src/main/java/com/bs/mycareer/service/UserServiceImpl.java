package com.bs.mycareer.service;

import com.bs.mycareer.dto.UserRegisterDto;
import com.bs.mycareer.entity.User;
import com.bs.mycareer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //생성자를 사용함으로써 의존성 주입 (자동 bean 주입인 @Autowire 사용하면 고효율)
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void registerProcess(UserRegisterDto userRegisterDto){

        String email = userRegisterDto.getEmail();
        String password = userRegisterDto.getPassword();


        // 이부분 의논 필요... -> 무슨 오류가 있을경우 던지게 해야되기 때문임(존재한다면 예외처리)
        if(userRepository.findByEmail(email).isPresent()){
            throw new RuntimeException("등록된 이메일입니다.");
        }

        User user = new User();
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        //user.setId(id);  -> Id의 값은 Generated value에 의해 지정해줄 필요 없다(수동으로 x)
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(encodedPassword));
        user.setRole("ROLE_USER");

        userRepository.save(user);


    }
}