package com.swyp.mema.domain.user.service;

import com.swyp.mema.domain.user.dto.CustomUserDetails;
import com.swyp.mema.domain.user.entity.UserEntity;
import com.swyp.mema.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userData = userRepository.findByEmail(email);

        if(userData != null) {

            return new CustomUserDetails(userData);
        }

        return null;
    }
}