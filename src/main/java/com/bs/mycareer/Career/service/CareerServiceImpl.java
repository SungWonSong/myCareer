package com.bs.mycareer.Career.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bs.mycareer.Career.dto.CareerDto;
import com.bs.mycareer.Career.entity.Career;
import com.bs.mycareer.Career.repository.CareerContentRepository;
import com.bs.mycareer.User.dto.BSUserDetail;
import com.bs.mycareer.User.entity.User;
import com.bs.mycareer.User.repository.UserRepository;
import com.bs.mycareer.exceptions.CustomException;
import com.bs.mycareer.User.service.BSUserDetailsService;
import com.bs.mycareer.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bs.mycareer.exceptions.ResponseCode.*;

// 핵심 비지니스 로직 구현
@Service
@RequiredArgsConstructor
public class CareerServiceImpl implements CareerService {

    @Autowired
    private final CareerContentRepository careerContentRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final JWTUtil jwtUtil;

    @Autowired
    private final BSUserDetailsService bsUserDetailsService;


    //커리어 작성
    @Override
    @Transactional
    public Career createCareer(String title, String content, HttpServletRequest httpServletRequest) {
        // 현재 사용자 정보 가져오기
        User user = getCurrentUserFromToken(httpServletRequest);
        Career career = new Career(title, content, user);
        // 사용자의 경력 목록에 추가
        user.getCareers().add(career);
        // 커리어 저장
        careerContentRepository.save(career);
        userRepository.save(user);

        return career;
    }


    //커리어 id별 조회
    @Override
    @Transactional(readOnly = true)
    public CareerDto getCareerById(Long id) {
        Career foundCareer = findCareer(id);
        return new CareerDto(foundCareer);
    }

    //전체 커리어 조회
    @Override
    @Transactional(readOnly = true)
    public List<CareerDto> getAllCareers() {
        List<CareerDto> careerDtoList = new ArrayList<>();
        List<Career> careers = careerContentRepository
                .findAllByAvailableTrue(Sort.by("id").ascending());
        for (Career career : careers) {
            careerDtoList.add(new CareerDto(career));
        }
        return careerDtoList;
    }


    @Transactional
    @Override
    public void editCareer(Long id, CareerDto careerDto, HttpServletRequest httpServletRequest) {
        Career career = findCareer(id);
        String title = careerDto.getTitle();
        String content = careerDto.getContent();

        // 사용자 찾아서 가지고 있는지 확인후 update로 진행후 저장(
        User user = getCurrentUserFromToken(httpServletRequest);

        // 현재 사용자가 해당 경력을 가지고 있으면 update 진행
        if (user.getCareers().contains(career)) {

            // 제목과 내용 모두 빈 칸이면 예외를 발생시킨다.
            if (title.trim().isEmpty() && content.trim().isEmpty()) {
                throw new CustomException(INVALID_EDIT_VALUE);
            }

            // 제목만 빈 칸이면 원본 제목을 유지한다.
            if (title.trim().isEmpty()) {
                title = career.getTitle();
            }

            // 내용만 빈 칸이면 원본 내용을 유지한다.
            if (content.trim().isEmpty()) {
                content = career.getContent();
            }

            career.edit(title, content);
            careerContentRepository.save(career);
            userRepository.save(user);

            // 업데이트된 경력 반환
        } else {
            // 사용자가 해당 경력을 가지고 있지 않을 경우
            throw new CustomException(INVALID_AUTH_TOKEN);
        }
    }

    //커리어 삭제
    @Override
    @Transactional
    public void deleteCareer(Long id, HttpServletRequest httpServletRequest) {
        Career foundCareer = findCareer(id);

        // 사용자 찾아서 가지고 있는지 확인후 delete 진행후 저장(
        User user = getCurrentUserFromToken(httpServletRequest);
        // 현재 사용자가 해당 경력을 가지고 있으면 delete 진행
        if (user.getCareers().contains(foundCareer)) {
            foundCareer.delete();
            careerContentRepository.flush(); // 변경 사항을 데이터베이스에 반영
            userRepository.save(user);
        } else {
            // 사용자가 해당 경력을 가지고 있지 않을 경우
            throw new CustomException(INVALID_AUTH_TOKEN);
        }

    }


    public Career findCareer(Long id) {
        Optional<Career> foundCareer = careerContentRepository.findCareerById(id);
        if (foundCareer.isEmpty()) {
            throw new CustomException(CAREER_NOT_FOUND);
        }
        Career career = foundCareer.get();
        System.out.println("career.isAvailable() = " + career.isAvailable());
        if (!career.isAvailable()) {
            throw new CustomException(CAREER_IS_DELETED);
        }
        return career;
    }

    private User getCurrentUserFromToken(HttpServletRequest httpServletRequest) {
        String token = extractTokenFromHeader(httpServletRequest.getHeader("Authorization"));
        validateAccessToken(token);

        BSUserDetail bsUserDetail = null;
        try {
            DecodedJWT decodedJWT = jwtUtil.verifyAccessToken(token);
            String email = decodedJWT.getClaim("email").asString();
            bsUserDetail = bsUserDetailsService.loadUserByUsername(email);
        } catch (Exception e) {
            throw new CustomException(FAILED_TO_SEARCH_USER_DETAILS);
        }

        return bsUserDetail.getUser();
    }

    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return jwtUtil.removePrefix(authorizationHeader);
        }
        throw new IllegalArgumentException("Invalid or missing Authorization header"); //여긴 굳이 customException 할필요 없을듯 반복사용안할거같아서
    }

    private void validateAccessToken(String token) {
        if (!jwtUtil.isAccessToken(token)) {
            throw new CustomException(INVALID_ACCESS_TOKEN);
        }
    }
}


