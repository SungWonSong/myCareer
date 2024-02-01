package com.bs.mycareer.service;

import com.bs.mycareer.dto.CareerDto;
import com.bs.mycareer.entity.Career;

import java.util.List;
import java.util.Optional;

public interface CareerService {

    Career createCareer(CareerDto careerDto);

    Optional<Career> getCareerById(Long id);

    List<Career> getAllCareers();

//    Career updateCareer(Long id, CareerDto careerDto);

    void deleteCareer(Long id);
}
