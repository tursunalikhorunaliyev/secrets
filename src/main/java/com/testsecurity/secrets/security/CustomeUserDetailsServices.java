package com.testsecurity.secrets.security;

import com.testsecurity.secrets.entities.UserEntity;
import com.testsecurity.secrets.repositories.UserEntityRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomeUserDetailsServices implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;


    public CustomeUserDetailsServices(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userEntityRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("Username not found"));
        return new User(user.getUsername(), user.getPassword(), getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(UserEntity user){
        return user.getRoles().stream().map(e-> new SimpleGrantedAuthority(e.getName())).collect(Collectors.toSet());
    }
}
