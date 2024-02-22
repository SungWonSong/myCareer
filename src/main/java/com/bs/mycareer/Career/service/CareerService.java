package com.bs.mycareer.Career.service;

import com.bs.mycareer.Career.entity.Career;
import com.bs.mycareer.Career.dto.CareerDto;
import com.bs.mycareer.User.entity.User;

import java.util.List;

public interface CareerService {

    //커리어 작성
    Career createCareer(String title, String content, User author);


    CareerDto getCareerById(Long id);

    List<CareerDto> getAllCareers();

    void editCareer(Long id, CareerDto careerDto);

    //커리어 삭제
    void deleteCareer(Long id);
}
