package com.bs.mycareer.Career;

import com.bs.mycareer.dto.BSUserDetail;
import com.bs.mycareer.entity.User;
import com.bs.mycareer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// 핵심 비지니스 로직 구현
@Service
@RequiredArgsConstructor
public class CareerServiceImpl implements CareerService {

    private final CareerContentRepository careerContentRepository;

    private final UserRepository userRepository;


    //커리어 작성
    @Override
    @Transactional
    public Career createCareer(CareerDto careerDto) {
        Career career = new Career();
        career.setTitle(careerDto.getTitle());
        career.setContents(careerDto.getContents());

        // 현재 사용자 정보 가져오기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            // UserDetails를 구현한 사용자 정보 클래스로 형변환
            BSUserDetail userDetails = (BSUserDetail) principal;
            User user = userDetails.getUser();

            career.setUser(user);

            // 사용자의 경력 목록에 추가
            user.getCareers().add(career);

            // 커리어 저장
            careerContentRepository.save(career);
            userRepository.save(user);

            return career;
        } else {
            // principal이 UserDetails가 아닌 경우 예외 처리 또는 적절한 로직 추가
            throw new IllegalStateException("Current user is not an instance of UserDetails");
        }
    }

    //커리어 id별 조회
    @Override
    @Transactional(readOnly = true)
    public CareerDto getCareerById(Long id) {
        Career career = findCareer(id);
        return new CareerDto(career.getTitle(), career.getContents());
    }


    //전체 커리어 조회
    @Override
    @Transactional(readOnly = true)
    public List<CareerDto> getAllCareers() {
        List<CareerDto> careerDtoList = new ArrayList<>();
        List<Career> careers = careerContentRepository.findAll();
        for (Career career : careers) {
            careerDtoList.add(new CareerDto(career));
        }
        return careerDtoList;
    }

//    @Override
//    @Transactional(readOnly = true)
//    public Career updateCareer(User user){
//        Career career = findCareer(user.getCareers()
//    }
//
//    //커리어 수정
//    @Override
//    public Career updateCareer(Long id, CareerDto careerDto) {
//        Career foundedCareer = findCareer(id);
//        if (foundedCareer.) {
//            Career career = optionalCareer.get();
//            career.setTitle(careerDto.getTitle());
//            career.setContents(careerDto.getContents());
//            return careerContentRepository.save(career);
//        }
//        return null; //예외 처리 방법 논의ㄱㄱ
//    }



    //커리어 삭제
    @Override
    public void deleteCareer(Long id) {
        careerContentRepository.deleteById(id);
    }


    public Career findCareer(Long id) {
        return careerContentRepository.findCareerById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 id의 career를 찾지못했습니다: " + id));
    }

    }


