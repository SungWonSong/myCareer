package com.bs.mycareer.service;

import com.bs.mycareer.dto.BSUserDetail;
import com.bs.mycareer.dto.CareerDto;
import com.bs.mycareer.dto.UserRegisterDto;
import com.bs.mycareer.entity.Career;
import com.bs.mycareer.entity.User;
import com.bs.mycareer.repository.CareerContentRepository;
import com.bs.mycareer.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// 핵심 비지니스 로직 구현
@Service
@RequiredArgsConstructor
public class CareerServiceImpl implements CareerService {

    private final CareerContentRepository careerContentRepository;
    private final UserRepository userRepository;


    //커리어 작성
    @Override
    @Transactional //데이터베이스에서 효율적인 트랜잭션 처리를 위함 -> 트랜잭션 : 데이터베이스에서 여러 작업을 하나의 논리적인 단위로 묶어서 처리하는 개념
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
        Career foundCareer = findCareer(id);
        return new CareerDto(foundCareer.getTitle(), foundCareer.getContents(), foundCareer.isAvailable());
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

    //커리어 수정
    @Override
    public Career updateCareer(Long id, CareerDto careerDto) {
        Optional<Career> optionalCareer = careerContentRepository.findCareerById(id);
        if (optionalCareer.isPresent()) {
            Career career = optionalCareer.get();
            career.setTitle(careerDto.getTitle());
            career.setContents(careerDto.getContents());
            return careerContentRepository.save(career);
        }
        return null; //예외 처리 방법 논의ㄱㄱ
    }

    //커리어 삭제
    @Override
    @Transactional
    public void deleteCareer(Long id) throws AccessDeniedException {
        Career foundCareer = findCareer(id);

        // 사용자 찾아서 가지고 있는지 확인후 delete 진행후 저장(
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof BSUserDetail userDetails) {
            User user = userDetails.getUser();
            // 현재 사용자가 해당 경력을 가지고 있으면 delete 진행
            if (user.getCareers().contains(foundCareer)) {
                foundCareer.delete();
                careerContentRepository.save(foundCareer);
                userRepository.save(user);
            } else {
                // 사용자가 해당 경력을 가지고 있지 않을 경우
                throw new AccessDeniedException("현재 사용자가 해당 경력을 삭제할 권한이 없습니다.");
            }

        } else {
            throw new IllegalStateException("인증된 사용자 정보를 찾을 수 없습니다.");
        }
    }

    //repo에서 career 찾아올때 사용하는 메서드
    public Career findCareer(Long id) {
        return careerContentRepository.findCareerById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 id의 career를 찾지못했습니다: " + id));
    }

    }


