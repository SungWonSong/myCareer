package com.bs.mycareer.service;

import com.bs.mycareer.dto.CareerDto;
import com.bs.mycareer.dto.UserRegisterDto;
import com.bs.mycareer.entity.Career;
import com.bs.mycareer.entity.User;

import java.util.List;
import java.util.Optional;

public interface CareerService {

    Career createCareer(CareerDto careerDto);

    CareerDto getCareerById(Long id);

    List<CareerDto> getAllCareers();



    Career updateCareer(Long id, CareerDto careerDto);

//    void deleteCareer(Long id);

    //커리어 삭제
    void deleteCareer(Long id);
}
