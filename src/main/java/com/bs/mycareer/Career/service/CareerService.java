package com.bs.mycareer.Career.service;

import com.bs.mycareer.Career.entity.Career;
import com.bs.mycareer.Career.dto.CareerDto;
import com.bs.mycareer.User.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface CareerService {

    //커리어 작성
//<<<<<<< HEAD
//    Career createCareer(String title, String content, User author);
//=======
    Career createCareer(CareerDto careerDto, HttpServletRequest httpServletRequest);
//>>>>>>> a08333bbb122d5deb66a88d7f0c4245e3e54d052


    CareerDto getCareerById(Long id);

    List<CareerDto> getAllCareers();

    void editCareer(Long id, CareerDto careerDto);

    //커리어 삭제
    void deleteCareer(Long id);
}
