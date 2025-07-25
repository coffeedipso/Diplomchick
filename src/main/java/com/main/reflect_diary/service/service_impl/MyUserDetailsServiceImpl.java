package com.main.reflect_diary.service.service_impl;

import com.main.reflect_diary.model.User;
import com.main.reflect_diary.repository.UserRepository;
import com.main.reflect_diary.secutiry.MyUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    public MyUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Загрузка пользователя для Spring Security по username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new MyUserDetails(user);
    }
}
