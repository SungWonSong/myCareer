package com.bs.mycareer.repository;

import com.bs.mycareer.entity.Career;

// 이부분을 상속해서 사용할랬는데 결국 jpa에 의해 그럴필요가 없게됨...
public interface CareerRepository {
    Career ContentSave (Career career);

}
