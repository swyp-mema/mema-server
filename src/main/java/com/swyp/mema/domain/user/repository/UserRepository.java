package com.swyp.mema.domain.user.repository;

import com.swyp.mema.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByUserId(Long userId);

    Boolean existsByEmail(String email);

    UserEntity findByUserId(Long userId);

    UserEntity findByEmail(String email);

}
