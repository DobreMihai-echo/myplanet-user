package com.myplanet.userservice.security;

import com.myplanet.userservice.domain.AuthUser;
import com.myplanet.userservice.domain.Role;
import com.myplanet.userservice.domain.Users;
import com.myplanet.userservice.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Username: " + username + " not found. Register first!"));
        return new User(users.getUsername(), users.getPassword(), mapToAuthorities(users.getRoles()));
    }

    private Collection<GrantedAuthority> mapToAuthorities(List<Role> roleList) {
        return roleList.stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
    }
}
