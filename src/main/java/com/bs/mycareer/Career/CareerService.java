package com.bs.mycareer.Career;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface CareerService {

    //커리어 작성
    Career createCareer(CareerDto careerDto);


    CareerDto getCareerById(Long id);

    List<CareerDto> getAllCareers();

    Career updateCareer(Long id,CareerDto careerDto) throws AccessDeniedException;

//    void deleteCareer(Long id);

    //커리어 삭제
    void deleteCareer(Long id);
}
