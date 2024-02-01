package com.bs.mycareer.service;

import com.bs.mycareer.dto.CareerDto;
import com.bs.mycareer.entity.Career;

import java.util.List;

public interface CareerService {

    Career createCareer(CareerDto careerDto);

    CareerDto getCareerById(Long id);

    List<CareerDto> getAllCareers();



//    Career updateCareer(Long id, CareerDto careerDto);

    //커리어 수정
    Career updateCareer(Long id, CareerDto careerDto);

    void deleteCareer(Long id);
}
