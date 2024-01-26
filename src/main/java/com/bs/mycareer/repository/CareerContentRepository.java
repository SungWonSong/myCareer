package com.bs.mycareer.repository;

import com.bs.mycareer.dto.CareerDto;
import com.bs.mycareer.entity.Career;

//db에 접근, 객체와 db연결하는 부분
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//Jpa를 상속하면 ContentSave는 Career save하는 함수를 자동으로 할당해준다...(개사기네)
@Repository
public interface CareerContentRepository extends JpaRepository<Career, Integer> {
    Career updateCareer(Long id, CareerDto careerDto);
    Career save(Career career);

    void deleteById(Long id);

    List<Career> findAll(); //글 조회
    Optional<Career> findById(Long id); //id 별 글 조회


    // 빈 구현으로 flush() 메서드 추가
    @Override
    default void flush() {
        // 빈 구현
    }

}

