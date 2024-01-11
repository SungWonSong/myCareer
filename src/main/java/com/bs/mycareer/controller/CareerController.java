package com.bs.mycareer.controller;


import com.bs.mycareer.entity.Career;
import com.bs.mycareer.repository.CareerContentRepository;
import com.bs.mycareer.service.CareerService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 말 그대로 controller
@RequiredArgsConstructor
@Controller
public class CareerController {

    private final CareerService careerService;
    private final CareerContentRepository careerContentRepository;
    //전체 조회
    @GetMapping("/careerContents")
    public List<Career> getCareers() {
        return careerService.findCareers();
    }



}
