package com.myplanet.userservice.security;


import com.myplanet.userservice.domain.UsersBase;
import com.myplanet.userservice.repository.UserBaseRepository;
import com.myplanet.userservice.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserBaseRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersBase usersBase = usersRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Username: " + username + " not found. Register first!"));
        return new User(usersBase.getUsername(), usersBase.getPassword(), usersBase.getEnabled(), usersBase.isAccountNonExpired(), usersBase.isCredentialsNonExpired(), usersBase.isAccountNonLocked(), usersBase.getAuthorities());
    }
}
