package com.bs.mycareer.Career;

import java.util.List;

public interface CareerService {

    //커리어 작성
    Career createCareer(CareerDto careerDto, Long user_id);


    CareerDto getCareerById(Long id);

    List<CareerDto> getAllCareers();

//    @Transactional(readOnly = true)
//    Career updateCareer(User user);

    void deleteCareer(Long id);
}
