package com.rtumirea.KazakovIG.cursework.services.impl;

import com.rtumirea.KazakovIG.cursework.config.MyUserDetails;
import com.rtumirea.KazakovIG.cursework.domain.dto.UserDto;
import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;
import com.rtumirea.KazakovIG.cursework.mappers.impl.UserMapper;
import com.rtumirea.KazakovIG.cursework.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findByPhoneNumber(phoneNumber);
        return userEntity.map(user -> {
            UserDto userDto = userMapper.mapTo(user);
            return new MyUserDetails(userDto);
        }).orElseThrow(() -> new UsernameNotFoundException(phoneNumber + " not found"));
    }
}
