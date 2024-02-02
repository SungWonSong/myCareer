package com.bs.mycareer.Career;

import com.bs.mycareer.entity.User;
import com.bs.mycareer.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Career createCareer(CareerDto careerDto,Long user_id) {
        Career career = new Career();
        career.setTitle(careerDto.getTitle());
        career.setContents(careerDto.getContents());

        // 사용자 조회
        Optional<User> foundUser = userRepository.findByUserId(user_id);
        if (foundUser.isPresent()) {
            User user = foundUser.get(); // OPtional<user>를 user타입에 넣는다... null이 아니기에
            career.setUser(user); // - 1. career는 user_id값에 참조되기에 결국 career에서 user를 setUser를 통해 유저를 지정
            user.getCareers().add(career); // - 2. user도 그 careers라는 빈 array에 career를 추가한다.

            // 커리어 저장
            careerContentRepository.save(career); // - 3, 4. career의 entity를 db에 저장, user도 career추가한 상태로 저장
            userRepository.save(user);
            // 사용자 엔티티를 저장하지 않아도 JPA가 관리하는 영속성 컨텍스트에 의해 관리됩니다.

        } else {
            // 사용자가 존재하지 않을 경우 예외 처리 또는 적절한 로직 추가
            throw new EntityNotFoundException("User not found with ID");
        }
        return career;
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


