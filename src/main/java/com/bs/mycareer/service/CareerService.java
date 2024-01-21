package com.bs.mycareer.service;

import com.bs.mycareer.entity.Career;
import com.bs.mycareer.repository.CareerContentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

// 핵심 비지니스 로직 구현
@Service
@RequiredArgsConstructor
public class CareerService {

    private final CareerContentRepository CareerContentRepository;
    @Transactional
    public Career createCareer(){
        Career career = new Career();
        CareerContentRepository.ContentSave(career);
        return career;
    }

    //전체 글 조회
    @Transactional
    public List<Career> findCareers() {
        return CareerContentRepository.findAll();
    }

    //id별 조회
    @Transactional
    public Optional<Career> findOne(Long careerId) {
        return CareerContentRepository.findById(careerId);
    }

    //글 수정
    @Transactional
    public Career updateCareer(Long id, Career updateCareer) {
        Optional<Career> existingCareer = CareerContentRepository.findById(id);

        if (existingCareer.isPresent()) {
            Career changingCareer = existingCareer.get();
            changingCareer.setTitle(updateCareer.getTitle());
            changingCareer.setContents(updateCareer.getContents());

            return CareerContentRepository.save((changingCareer));
        } else { //해당 id가 없을 경우 수정 못함
            throw new NoSuchElementException("해당 id의 Career를 찾을 수 없습니다 : " + id);
        }
    }




}
