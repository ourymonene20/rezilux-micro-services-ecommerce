package com.rezilux.dinngdonngecommerceapi.security;


import com.rezilux.dinngdonngecommerceapi.entities.User;
import com.rezilux.dinngdonngecommerceapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//import com.rezilux.yupay.entities.auth.User;
//import com.rezilux.yupay.repositories.auth.UserRepository;

public class CustomUserDetailService implements UserDetailsService {

    private UserRepository repository;

    @Autowired
    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> userSearch = repository.connexion(login, true);
        return userSearch.map(utilisateur -> {
            System.out.println(utilisateur.getFirstName());
            System.out.println(utilisateur.getProfiles().size());
            List<GrantedAuthority> profils = utilisateur.getProfiles().stream().map(x ->
                    new SimpleGrantedAuthority(x.getName())
            ).collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(login, utilisateur.getPassword(), profils);
        }).orElseThrow(() -> new UsernameNotFoundException("L'utilisateur " + login + " n'existe pas !"));
    }
}
