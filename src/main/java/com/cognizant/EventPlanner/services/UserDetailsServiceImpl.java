package com.cognizant.EventPlanner.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cognizant.EventPlanner.model.Role;
import com.cognizant.EventPlanner.model.User;
import com.cognizant.EventPlanner.repository.UserRepository;

import java.util.Collections;
import java.util.Set;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(),
                true, true, true, true, getAuthorities(user.getRole()));
    }

    private Set<GrantedAuthority> getAuthorities(Role role) {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * For Mocking and Checking functionality
     * username - mockuser
     * password - password
     */
    // @Override
    // public UserDetails loadUserByUsername(String username) throws
    // UsernameNotFoundException {
    // if ("mockuser".equals(username)) {
    // return User
    // .withUsername("mockuser")
    // .password("$2a$12$gljcIqq.b589M3EPqETCoOUX74kECEPX.cMCUJQqgt1zppLbJcyKm")
    // .roles("USER")
    // .build();
    // } else {
    // throw new UsernameNotFoundException("User not found");
    // }
    // }

}
