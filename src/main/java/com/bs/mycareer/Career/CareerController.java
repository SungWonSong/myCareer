package com.bs.mycareer.Career;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 말 그대로 controller
@RequiredArgsConstructor
@RestController
public class CareerController {

    @Autowired //자동으로 bean 등록?
    private CareerService careerService;
    private final CareerContentRepository careerContentRepository;

    //Career를 List<Career>에 추가한다.
//    @PostMapping("/career")
//        public List<Career> postCareers() {
//            List<Career> careers = new ArrayList<>();
//            Career career = new Career();
//            careers.add(career);
//            return careers;
//
//        }
    //커리어 작성
    @PostMapping("user/career/create")
    public Career createCareer(@RequestBody CareerDto careerDto, Long user_id) {
        return careerService.createCareer(careerDto);
    }


    //전체 조회

    @GetMapping("career/ContentLists")
    public List<CareerDto> getAllCareers() {
        return careerService.getAllCareers();
    }

    //id별 조회

    @GetMapping("career/{id}")
    public CareerDto getCareerById(@PathVariable Long id) {
        return careerService.getCareerById(id); // 존재하지 않을 경우 null 반환, 혹은 예외 처리 가능
    }

    //글 수정
//    @PutMapping("/career/{id}")
//    public Career updateCareer(@PathVariable Long id, @RequestBody CareerDto careerDto) {
//        return careerService.(id, careerDto);
//    }

    @DeleteMapping("career/{id}")
    public void deleteCareer(@PathVariable Long id) {
        careerService.deleteCareer(id);
    }


}
