package com.bs.mycareer.service;

import com.bs.mycareer.dto.RegisterRequest;
import com.bs.mycareer.entity.User;
import com.bs.mycareer.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    //생성자를 사용함으로써 의존성 주입 (자동 bean 주입인 @Autowired 사용하면 고효율)
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void registerProcess(RegisterRequest registerRequest) {

        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();

        // 예외처리는 일단 한개로 나중에 exception파일에 따로 예정
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("등록된 이메일입니다.");
        }

        User user = new User();
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        //user.setId(id);  -> Id의 값은 Generated value에 의해 지정해줄 필요 없다(수동으로 x)
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRole("USER");

        userRepository.save(user);
    }
}