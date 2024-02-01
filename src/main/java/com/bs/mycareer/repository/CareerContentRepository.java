package com.bs.mycareer.repository;

import com.bs.mycareer.entity.Career;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//Jpa를 상속하면 ContentSave는 Career save하는 함수를 자동으로 할당해준다...(개사기네)
@Repository
public interface CareerContentRepository extends JpaRepository<Career, Long> {
//    Career updateCareer(Long id, CareerDto careerDto);

    void deleteById(Long id);


    List<Career> findAll(); //글 조회
    Optional<Career> findById(Long id); //id 별 글 조회

}

