package com.swyp.mema.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.mema.domain.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
