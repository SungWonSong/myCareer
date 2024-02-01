package com.bs.mycareer.service;

import com.bs.mycareer.dto.CareerDto;
import com.bs.mycareer.entity.Career;
import com.bs.mycareer.repository.CareerContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// 핵심 비지니스 로직 구현
@Service
@RequiredArgsConstructor
public class CareerServiceImpl implements CareerService {

    @Autowired
    private CareerContentRepository careerContentRepository;
    @Override
    public Career createCareer(CareerDto careerDto) {
        Career career = new Career();
        career.setTitle(careerDto.getTitle());
        career.setContents(careerDto.getContents());
        return careerContentRepository.save(career);
    }


    //커리어 id별 조회
    @Override
    public Optional<Career> getCareerById(Long id) {
        return careerContentRepository.findById(id);
    }

    //전체 커리어 조회
    @Override
    public List<Career> getAllCareers() {
        return careerContentRepository.findAll();
    }

//    @Override
//    public Career updateCareer(Long id, CareerDto careerDto) {
//        Optional<Career> optionalCareer = careerContentRepository.findById(id);
//        if (optionalCareer.isPresent()) {
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



    }


