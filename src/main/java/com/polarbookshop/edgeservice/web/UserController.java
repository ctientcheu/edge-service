package com.polarbookshop.edgeservice.web;

import com.polarbookshop.edgeservice.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author clement.tientcheu@cerebrau.com
 * @project edge-service
 * @org Cerebrau
 */
@RestController
public class UserController {

    @GetMapping("user")
    public Mono<User> getUser() {
        return ReactiveSecurityContextHolder
            .getContext()
            .map(SecurityContext::getAuthentication)
            .map(authentication -> (org.springframework.security.core.userdetails.User) authentication.getPrincipal())
            .map(springUser -> new User(
                springUser.getUsername(),
                springUser.getUsername(),
                springUser.getUsername(),
                springUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).map(Objects::toString).toList()
            ));
    }
}
