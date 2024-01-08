package com.bs.mycareer.repository;

import com.bs.mycareer.entity.Career;

//db에 접근, 객체와 db연결하는 부분
import org.springframework.data.jpa.repository.JpaRepository;

//Jpa를 상속하면 ContentSave는 Career save하는 함수를 자동으로 할당해준다...(개사기네)
public interface CareerContentRepository extends JpaRepository<Career, Long> {
    void ContentSave(Career career);
}

