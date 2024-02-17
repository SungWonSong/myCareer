package com.bs.mycareer;

import com.bs.mycareer.User.entity.User;
import com.bs.mycareer.User.repository.UserRepository;
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

    // InitData 클래스의 run 메소드에서 Exception을 던지고 있기 때문에, 해당 예외가 발생하면 Spring Boot 애플리케이션은 시작에 실패하게 됩니다.
    // 예외처리는 안하는 쪽으로 수정했습니다.
        @Override
        public void run(ApplicationArguments args) {
            User user1 = new User("sungwon1@naver.com", bCryptPasswordEncoder.encode("qwer1234"), "USER");
            User user2 = new User("boa2@naver.com", bCryptPasswordEncoder.encode("qwer1234"), "USER");

            if (!userRepository.findByEmail(user1.getEmail()).isPresent()){
                userRepository.save(user1);
            }

            if (!userRepository.findByEmail(user2.getEmail()).isPresent()){
                userRepository.save(user2);
            }
        }
    }

