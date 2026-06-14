package com.pbs.springmvc.service;

import com.pbs.springmvc.model.User;
import com.pbs.springmvc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Buscamos el usuario en la base de datos H2
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // 2. Extraemos el nombre del rol y le concatenamos 'ROLE_' (ej: 'ROLE_ADMIN' o 'ROLE_USER')
        // Convertimos a mayúsculas para estandarizar el comportamiento de Spring Security
        String rolFormateado = "ROLE_" + user.getRole().getName().toUpperCase();

        // 3. Retornamos el objeto User nativo de Spring Security con los datos reales de tu BD
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                "{noop}" + user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(rolFormateado))
        );
    }
}