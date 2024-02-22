package com.bs.mycareer.User.service;

import com.bs.mycareer.User.dto.BSUserDetail;
import com.bs.mycareer.User.entity.User;
import com.bs.mycareer.User.repository.UserRepository;
import com.bs.mycareer.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.bs.mycareer.exceptions.ResponseCode.*;

@Service
public class BSUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    @Autowired
    public BSUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //원래 로직을 만들어서 사용하려고 했으나 implement로 상속받았기에 UserDetailService에 있는 메소드 명을 사용하지 않으면
    //상속의 조건에서 벗어나기에 그대로 @Override해서 함수명에 로직을 바꿔서 사용해야된다.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        return new BSUserDetail(user);
    }

}

