package com.bs.mycareer.service;

import com.bs.mycareer.dto.CareerDto;
import com.bs.mycareer.entity.Career;
import com.bs.mycareer.repository.CareerContentRepository;

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

    //커리어 작성
    @Override
    @Transactional //데이터베이스에서 효율적인 트랜잭션 처리를 위함 -> 트랜잭션 : 데이터베이스에서 여러 작업을 하나의 논리적인 단위로 묶어서 처리하는 개념
    public Career createCareer(CareerDto careerDto) {
        Career career = new Career();
        career.setTitle(careerDto.getTitle());
        career.setContents(careerDto.getContents());
        careerContentRepository.save(career);

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
    public void deleteCareer(Long id) {
        careerContentRepository.deleteById(id);
    }


    public Career findCareer(Long id) {
        return careerContentRepository.findCareerById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 id의 career를 찾지못했습니다: " + id));
    }

    }


