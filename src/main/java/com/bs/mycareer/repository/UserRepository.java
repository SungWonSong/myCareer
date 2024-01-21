package com.bs.mycareer.repository;

import com.bs.mycareer.entity.Career;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Career, Long> {

}
