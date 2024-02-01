package com.bs.mycareer.repository;

import com.bs.mycareer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

//    User mustFindByEmail(String email);

}
