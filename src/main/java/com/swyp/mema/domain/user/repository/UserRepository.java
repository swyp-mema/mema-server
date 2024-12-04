package com.swyp.mema.domain.user.repository;

import com.swyp.mema.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUserId(Long userId);

    Boolean existsByEmail(String email);

    User findByUserId(Long userId);

    User findByEmail(String email);

}
