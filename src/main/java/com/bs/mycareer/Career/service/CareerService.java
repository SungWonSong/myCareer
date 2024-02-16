package com.bs.mycareer.Career.service;

import com.bs.mycareer.Career.entity.Career;
import com.bs.mycareer.Career.dto.CareerDto;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface CareerService {

    //커리어 작성
    Career createCareer(CareerDto careerDto);


    CareerDto getCareerById(Long id);

    List<CareerDto> getAllCareers();

    Career updateCareer(Long id,CareerDto careerDto) throws AccessDeniedException;

    void deleteCareer(Long id);
}
