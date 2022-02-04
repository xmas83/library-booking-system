package com.techomeck.test.Library.booking.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techomeck.test.Library.booking.system.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
