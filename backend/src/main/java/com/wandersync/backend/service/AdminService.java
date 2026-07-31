package com.wandersync.backend.service;

import com.wandersync.backend.dto.UserResponse;
import com.wandersync.backend.exception.ResourceNotFoundException;
import com.wandersync.backend.model.User;
import com.wandersync.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public UserResponse replaceUserRoles(String userId, Set<User.Role> roles) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setRoles(roles);
        return UserResponse.from(userRepository.save(user));
    }
}
