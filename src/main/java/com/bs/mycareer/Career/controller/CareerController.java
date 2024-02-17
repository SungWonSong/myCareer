package com.bs.mycareer.Career.controller;

import com.bs.mycareer.Career.entity.Career;
import com.bs.mycareer.Career.repository.CareerContentRepository;
import com.bs.mycareer.Career.dto.CareerDto;
import com.bs.mycareer.Career.service.CareerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

// 말 그대로 controller
@RequiredArgsConstructor
@RestController
public class CareerController {

    @Autowired //자동으로 bean 등록?
    private CareerService careerService;

    @Autowired
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
    @PostMapping("/career/create")
    public Career createCareer(@RequestBody CareerDto careerDto) {
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


    @PutMapping("/career/{id}")
    public ResponseEntity<Career> updateCareer(@PathVariable Long id, @RequestBody CareerDto careerDto) {
        try {
            Career updatedCareer = careerService.updateCareer(id, careerDto);
            return ResponseEntity.ok(updatedCareer);
        } catch (AccessDeniedException e) {
            // AccessDeniedException에 대한 처리 로직
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


    @DeleteMapping("/career/{id}")
    public void deleteCareer(@PathVariable Long id) {
        careerService.deleteCareer(id);
    }


}
