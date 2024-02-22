package com.bs.mycareer.Career.controller;

import com.bs.mycareer.Career.entity.Career;
import com.bs.mycareer.Career.repository.CareerContentRepository;
import com.bs.mycareer.Career.dto.CareerDto;
import com.bs.mycareer.Career.service.CareerService;
import com.bs.mycareer.User.dto.BSUserDetail;
import com.bs.mycareer.exceptions.CustomException;
import com.bs.mycareer.exceptions.ServerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

import static com.bs.mycareer.exceptions.ResponseCode.*;

// 말 그대로 controller
@RequiredArgsConstructor
@RestController
public class CareerController {

    //자동으로 bean 등록?
    private final CareerService careerService;

    @Autowired
    private final CareerContentRepository careerContentRepository;

    //커리어 작성
    @PostMapping("/career/create")
    public ResponseEntity<ServerResponse> createCareer(@RequestBody CareerDto careerDto, Principal principal) {
        // 게시글 유효성 검증 (빈칸 없게끔)
        String title = careerDto.getTitle();
        String content = careerDto.getContent();
        if (title.trim().equals("")) {
            throw new CustomException(INVALID_CAREER_TITLE);
        }

        if (content.trim().equals("")) {
            throw new CustomException(INVALID_CONTENT);
        }
        BSUserDetail bsUserDetails = (BSUserDetail) ((Authentication) principal).getPrincipal();

        careerService.createCareer(title, content,  bsUserDetails.getUser());
        return ServerResponse.toResponseEntity(SUCCESS_CREATE);
    }


    //전체 조회
    @GetMapping("career/ContentLists")
    public List<CareerDto> getAllCareers() {
        return careerService.getAllCareers();
    }


    //id별 조회
    @GetMapping("career/{id}")
    public CareerDto getCareerById(@PathVariable Long id) {
        return careerService.getCareerById(id);
    }

    //커리어 수정
    @PutMapping("/career/{id}")
    public ResponseEntity<ServerResponse> editCareer(@PathVariable Long id, @RequestBody CareerDto careerDto) {

        careerService.editCareer(id, careerDto);
        return ServerResponse.toResponseEntity(SUCCESS_EDIT);
    }

    //커리어 삭제
    @DeleteMapping("/career/{id}")
    public ResponseEntity<ServerResponse> deleteCareer(@PathVariable Long id) {
        careerService.deleteCareer(id);
        return ServerResponse.toResponseEntity(SUCCESS_DELETE);
    }


}
