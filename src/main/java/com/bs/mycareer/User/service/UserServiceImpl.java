package com.bs.mycareer.User.service;

import com.bs.mycareer.User.dto.RegisterRequest;
import com.bs.mycareer.User.entity.User;
import com.bs.mycareer.User.repository.UserRepository;
import com.bs.mycareer.Common.jwt.JWTUtil;
import com.bs.mycareer.Common.exceptions.CustomException;
import com.bs.mycareer.utils.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.bs.mycareer.Common.exceptions.ResponseCode.*;

//예외처리는 추후에 따로 모아서 진행할 예정....
@Service
public class UserServiceImpl implements UserService {

    //생성자를 사용함으로써 의존성 주입 (자동 bean 주입인 @Autowired 사용하면 고효율)
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    private final JWTUtil jwtUtil;


    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public void registerProcess(RegisterRequest registerRequest) {

        if (registerRequest == null) {
            throw new IllegalArgumentException("RegisterRequest가 null입니다.");
        }

        String email = registerRequest.getEmail();

        //이메일형식 안맞을때
        if (!UserValidator.isValidEmail(email)) {
            throw new CustomException(INVALID_EMAIL_PATTERN);
        }

        String password = registerRequest.getPassword();

        //비번 형식 안맞을때
        if (!UserValidator.isValidPassword(password)) {
            throw new CustomException(INVALID_PASSWORD_PATTERN);
        }

        //이메일 비번이 Null인 경우...
        if(email == null) {
            throw new CustomException(INVALID_SIGN_IN_EMAIL);
        }
        if(password == null) {
            throw new CustomException(INVALID_SIGN_IN_PASSWORD);
        }

        //중복회원이 있을 경우
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new CustomException(DUPLICATE_USER);
        }

        String encodedPassword = bCryptPasswordEncoder.encode(password);

        //user.setId(id);  -> Id의 값은 Generated value에 의해 지정해줄 필요 없다(수동으로 x)
        //builder 함수를 사용 : registerRequest에서 결국 빌드해서 Entity 객체화 해줘야된다.
        User user = registerRequest.toEntity(encodedPassword);
        if (user == null) {
            throw new RuntimeException("User 객체 생성에 실패했습니다.");
        }

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("데이터베이스 저장에 실패했습니다.", e);
        }
    }


//    @Override
//    @Transactional
//    public void logoutProcess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
//
//        Object savedAccessToken = httpServletRequest.getSession().getAttribute("savedAccessToken");
//        System.out.println("accessTokenResponse = " + savedAccessToken);
//
////        }
//
//
//        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login");
//    }

}

