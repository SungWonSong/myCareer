package com.bs.mycareer.controller;

import com.bs.mycareer.entity.Career;
import com.bs.mycareer.repository.CareerContentRepository;
import com.bs.mycareer.service.CareerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

// 말 그대로 controller
@RequiredArgsConstructor
@RestController
public class CareerController {

    private final CareerService careerService;
    private final CareerContentRepository careerContentRepository;

    //Carreer를 List<Carrer>에 추가한다.
    @PostMapping("/career")
        public List<Career> postCareers() {
            List<Career> careers = new ArrayList<>();
            Career career = new Career();
            careers.add(career);
            return careers;

        }


    //전체 조회
    @GetMapping("/career/ContentLists")
    public List<Career> getCareers() {
        return careerService.findCareers();
    }

    //글 수정
    @PutMapping("/career/update")
    public Career updateCareer(Long id, Career updatedCareer) {
        return careerService.updateCareer(id, updatedCareer);
    }



}
