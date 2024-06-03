package com.polarbookshop.edgeservice.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbookshop.edgeservice.User;
import com.polarbookshop.edgeservice.config.SecurityConfig;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * @author clement.tientcheu@cerebrau.com
 * @project edge-service
 * @org Cerebrau
 */
@WebFluxTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTests {

  @Autowired WebTestClient webClient;

  @MockBean ReactiveClientRegistrationRepository clientRegistrationRepository;

  @Test
  void whenNotAuthenticated_then401() {
    webClient.get().uri("/user").exchange().expectStatus().isUnauthorized();
  }

  @Test
  void whenAuthenticated_thenReturnUser() {
    var expectedUser = new User("jon.snow", "jon", "snow", List.of("employee", "customer"));

    webClient
        .mutateWith(configureMockOidcLogin(expectedUser))
        .get()
        .uri("/user")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody(User.class)
        .value(user -> assertThat(user).isEqualTo(expectedUser));
  }

  private SecurityMockServerConfigurers.OidcLoginMutator configureMockOidcLogin(User expectedUser) {
    return SecurityMockServerConfigurers.mockOidcLogin()
        .idToken(
            builder ->
                builder
                    .claim(StandardClaimNames.PREFERRED_USERNAME, expectedUser.username())
                    .claim(StandardClaimNames.GIVEN_NAME, expectedUser.firstName())
                    .claim(StandardClaimNames.FAMILY_NAME, expectedUser.lastName())
                    .claim("roles", expectedUser.roles()));
  }
}
