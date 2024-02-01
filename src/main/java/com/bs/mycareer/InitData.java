package com.bs.mycareer;

import com.bs.mycareer.entity.User;
import com.bs.mycareer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitData implements ApplicationRunner {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user1 = new User("sungwon1@naver.com", bCryptPasswordEncoder.encode("qwer1234"), "USER");
        User user2 = new User("boa2@naver.com", bCryptPasswordEncoder.encode("qwer1234"), "USER");


//        if (userRepository.findByEmail(user1.getEmail()).isPresent() || userRepository.findByEmail(user2.getEmail()).isPresent()) {
//            throw new Exception("등록된 이메일입니다.");
//        }

        userRepository.save(user1);
        userRepository.save(user2);

    }
}
