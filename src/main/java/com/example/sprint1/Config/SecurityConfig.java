package com.example.sprint1.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/", "/error", "/webjars/**").permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(entryPoint -> entryPoint.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint
                                                .oidcUserService(oidcUserService())
                                )
                );
    }

    @Bean
    public OidcUserService oidcUserService() {
        return new OidcUserService();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(keycloakClientRegistration());
    }

    private ClientRegistration keycloakClientRegistration() {
        return ClientRegistration
                .withRegistrationId("keycloak")
                .clientId("Plateforme-education")
                .clientSecret("Plateforme-education")
                .clientName("Plateforme-education")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUriTemplate("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("openid")
                .authorizationUri("http://localhost:8080/auth/realms/Angular-Project/protocol/openid-connect/auth")
                .tokenUri("http://localhost:8080/auth/realms/Angular-Project/protocol/openid-connect/token")
                .userInfoUri("http://localhost:8080/auth/realms/Angular-Project/protocol/openid-connect/userinfo")
                .jwkSetUri("http://localhost:8080/auth/realms/Angular-Project/protocol/openid-connect/certs")
                .clientAuthenticationMethod(ClientRegistration.ClientAuthenticationMethod.BASIC)
                .build();
    }
}
