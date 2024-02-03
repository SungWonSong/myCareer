package com.bs.mycareer.Career;

import java.util.List;

public interface CareerService {

    //커리어 작성
    Career createCareer(CareerDto careerDto);


    CareerDto getCareerById(Long id);

    List<CareerDto> getAllCareers();


    void deleteCareer(Long id);
}
