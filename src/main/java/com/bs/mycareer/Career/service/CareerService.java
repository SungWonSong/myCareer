package com.bs.mycareer.Career.service;

import com.bs.mycareer.Career.dto.CareerDto;
import com.bs.mycareer.Career.entity.Career;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface CareerService {

    //커리어 작성
    Career createCareer(String title, String content, HttpServletRequest httpServletRequest );

    CareerDto getCareerById(Long id);

    List<CareerDto> getAllCareers();

    void editCareer(Long id, CareerDto careerDto , HttpServletRequest httpServletRequest);

    //커리어 삭제
    void deleteCareer(Long id, HttpServletRequest httpServletRequest);
}
