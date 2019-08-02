package com.example.demo.persistance.repository;

import com.example.demo.persistance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUserName(String userName);
}
