package com.polarbookshop.edgeservice.web;

import com.polarbookshop.edgeservice.User;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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
            .map(authentication -> (OidcUser) authentication.getPrincipal())
            .map(oidcUser -> new User(
                oidcUser.getPreferredUsername(),
                oidcUser.getGivenName(),
                oidcUser.getFamilyName(),
                oidcUser.getClaimAsStringList("roles")
            ));
    }
}
