package com.bs.mycareer.User.service;

import com.bs.mycareer.User.dto.RegisterRequest;
import com.bs.mycareer.User.entity.User;
import com.bs.mycareer.User.repository.UserRepository;
import com.bs.mycareer.utils.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

//예외처리는 추후에 따로 모아서 진행할 예정....
@Service
public class UserServiceImpl implements UserService{

    //생성자를 사용함으로써 의존성 주입 (자동 bean 주입인 @Autowired 사용하면 고효율)
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public void registerProcess(RegisterRequest registerRequest) {

        if(registerRequest == null) {
            throw new IllegalArgumentException("RegisterRequest가 null입니다.");
        }

        String email = registerRequest.getEmail();


        if (!UserValidator.isValidEmail(email)) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }

        String password = registerRequest.getPassword();

        if (!UserValidator.isValidPassword(password)) {
            throw new IllegalArgumentException("비밀번호 형식이 올바르지 않습니다.");
        }

        if(email == null || password == null) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 null입니다.");
        }

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new RuntimeException("중복된 이메일입니다.");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(password);

        //user.setId(id);  -> Id의 값은 Generated value에 의해 지정해줄 필요 없다(수동으로 x)
        //builder 함수를 사용 : registerRequest에서 결국 빌드해서 Entity 객체화 해줘야된다.
        User user = registerRequest.toEntity(encodedPassword);
        if(user == null) {
            throw new RuntimeException("User 객체 생성에 실패했습니다.");
        }

        try {
            userRepository.save(user);
        } catch(Exception e) {
            throw new RuntimeException("데이터베이스 저장에 실패했습니다.", e);
        }
    }
}

