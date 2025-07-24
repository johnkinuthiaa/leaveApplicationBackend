package com.slippery.leaveapplication.services;

import com.slippery.leaveapplication.models.UserPrincipal;
import com.slippery.leaveapplication.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository repository;

    public MyUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var existingUser =repository.findByUsername(username);
        if(existingUser.isEmpty()){
            throw new UsernameNotFoundException("User was not found");
        }
        return new UserPrincipal(existingUser.get());
    }
}
