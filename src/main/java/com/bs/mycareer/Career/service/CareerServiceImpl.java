package com.bs.mycareer.Career.service;

import com.bs.mycareer.Career.entity.Career;
import com.bs.mycareer.Career.repository.CareerContentRepository;
import com.bs.mycareer.Career.dto.CareerDto;
import com.bs.mycareer.User.dto.BSUserDetail;
import com.bs.mycareer.User.entity.User;
import com.bs.mycareer.User.repository.UserRepository;
import com.bs.mycareer.exceptions.CustomException;
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


    //커리어 작성
    @Override
    @Transactional
    public Career createCareer(String title, String content, User author) {
        Career career = new Career(title, content, author);

//        // 현재 사용자 정보 가져오기
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        if (principal instanceof UserDetails) {
//            UserDetails userDetails = (UserDetails) principal;
//            User user = ((BSUserDetail) userDetails).getUser();
//
//            career.setUser(user);
////
////            // 사용자의 경력 목록에 추가
//            user.getCareers().add(career);

        // 커리어 저장
        careerContentRepository.save(career);
//            userRepository.save(user);

        return career;
//        } else {
//            // principal이 UserDetails가 아닌 경우 예외 처리 또는 적절한 로직 추가
//            throw new IllegalStateException("Current user is not an instance of UserDetails");
//        }
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
    public void editCareer(Long id, CareerDto careerDto) {
        Career career = findCareer(id);
        String title = careerDto.getTitle();
        String content = careerDto.getContent();

        // 사용자 찾아서 가지고 있는지 확인후 update로 진행후 저장(
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof BSUserDetail userDetails) {
//            User user = userDetails.getUser();
//
//            // 현재 사용자가 해당 경력을 가지고 있으면 update 진행
//            if (user.getCareers().contains(career)) {

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
//                userRepository.save(user);

//                // 업데이트된 경력 반환
        //            } else {
//                // 사용자가 해당 경력을 가지고 있지 않을 경우
//                throw new CustomException(INVALID_AUTH_TOKEN);
//            }
//        } else {
//            // 현재 사용자가 BSUserDetail의 인스턴스가 아닐 경우
//            throw new IllegalStateException("인증된 사용자 정보를 찾을 수 없습니다.");
//        }
    }

    //커리어 삭제
    @Override
    @Transactional
    public void deleteCareer(Long id) {
        Career foundCareer = findCareer(id);

//        // 사용자 찾아서 가지고 있는지 확인후 delete 진행후 저장(
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof BSUserDetail userDetails) {
//            User user = userDetails.getUser();
        // 현재 사용자가 해당 경력을 가지고 있으면 delete 진행
//            if (user.getCareers().contains(foundCareer)) {
        foundCareer.delete();
//                careerContentRepository.flush(); // 변경 사항을 데이터베이스에 반영
//                userRepository.save(user);
//            } else {
//                // 사용자가 해당 경력을 가지고 있지 않을 경우
//                throw new CustomException(INVALID_AUTH_TOKEN);
//            }

//        } else {
//            throw new IllegalStateException("인증된 사용자 정보를 찾을 수 없습니다.");
//        }
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

}


