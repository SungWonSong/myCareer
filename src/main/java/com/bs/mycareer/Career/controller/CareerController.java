package com.bs.mycareer.Career.controller;

import com.bs.mycareer.Career.dto.CareerDto;

import com.bs.mycareer.Career.service.CareerService;
import com.bs.mycareer.exceptions.CustomException;
import com.bs.mycareer.exceptions.ServerResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.ui.Model;
import static com.bs.mycareer.exceptions.ResponseCode.*;

// 말 그대로 controller
@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class CareerController {

    @Autowired
    private final CareerService careerService;


    //커리어 작성
    @Transactional
    @PostMapping("/career/create")
    public ResponseEntity<ServerResponse> createCareer(@RequestBody CareerDto careerDto, HttpServletRequest httpServletRequest) {
        // 게시글 유효성 검증 (빈칸 없게끔)
        String title = careerDto.getTitle();
        String content = careerDto.getContent();
        if (title.trim().isEmpty()) {
            throw new CustomException(INVALID_CAREER_TITLE);
        }

        if (content.trim().isEmpty()) {
            throw new CustomException(INVALID_CONTENT);
        }
        careerService.createCareer(title, content, httpServletRequest);
        return ServerResponse.toResponseEntity(SUCCESS_CREATE);

    }


    //전체 조회
//    @CrossOrigin(origins = "http://localhost:8080")
//    @GetMapping("/")
//    @ResponseBody
//    public List<CareerDto> getAllCareers() {
//        List<CareerDto> careerDtoList = careerService.getAllCareers();
//        return careerDtoList;
//    }
    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/")
    public String getAllCareers(Model model) {
        List<CareerDto> careerDtoList = careerService.getAllCareers();
        model.addAttribute("careers", careerDtoList);
        return "index";  // "index"는 타임리프 템플릿의 파일명입니다.
    }


    //id별 조회
//    @CrossOrigin(origins = "http://localhost:8080")
//    @GetMapping("/career/{id}")
//    public CareerDto getCareerById(@PathVariable(name= "id") Long id) {
//        return careerService.getCareerById(id);
//    }
    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/career/{id}")
    public String getCareerById(@PathVariable(name = "id") Long id, Model model) {
        CareerDto careerDto = careerService.getCareerById(id);
        model.addAttribute("career", careerDto);
        return "career";  // "career"는 타임리프 템플릿의 파일명입니다.
    }

    //커리어 수정
    @PutMapping("/career/{id}")
    public ResponseEntity<ServerResponse> editCareer(@PathVariable(name= "id") Long id, @RequestBody CareerDto careerDto, HttpServletRequest httpServletRequest) {

        careerService.editCareer(id, careerDto, httpServletRequest);
        return ServerResponse.toResponseEntity(SUCCESS_EDIT);
    }

    //커리어 삭제
    @DeleteMapping("/career/{id}")
    public ResponseEntity<ServerResponse> deleteCareer(@PathVariable(name= "id") Long id, HttpServletRequest httpServletRequest) {
        careerService.deleteCareer(id, httpServletRequest);
        return ServerResponse.toResponseEntity(SUCCESS_DELETE);
    }


}
