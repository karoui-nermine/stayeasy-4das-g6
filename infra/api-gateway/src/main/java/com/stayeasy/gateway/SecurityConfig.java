package com.stayeasy.gateway;
import org.springframework.context.annotation.*;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.*;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;
import java.util.*;
import java.util.stream.Collectors;
@Configuration @EnableWebFluxSecurity
public class SecurityConfig {
  @Bean SecurityWebFilterChain chain(ServerHttpSecurity http){
    return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
      .authorizeExchange(a -> a
        .pathMatchers(HttpMethod.OPTIONS).permitAll()
        .pathMatchers("/actuator/**").permitAll()
        .pathMatchers("/api/maintenances/**").hasAnyRole("EQUIPE_TECHNIQUE","PROPRIETAIRE")
        .pathMatchers(HttpMethod.POST,"/api/accommodations/**").hasRole("PROPRIETAIRE")
        .pathMatchers(HttpMethod.PUT,"/api/accommodations/**").hasRole("PROPRIETAIRE")
        .pathMatchers(HttpMethod.DELETE,"/api/accommodations/**").hasRole("PROPRIETAIRE")
        .anyExchange().authenticated())
      .oauth2ResourceServer(o -> o.jwt(j -> j.jwtAuthenticationConverter(converter())))
      .build();
  }
  @SuppressWarnings("unchecked")
  private Converter<Jwt, Mono<AbstractAuthenticationToken>> converter(){
    JwtAuthenticationConverter c=new JwtAuthenticationConverter();
    c.setJwtGrantedAuthoritiesConverter(jwt -> {
      Map<String,Object> ra=jwt.getClaim("realm_access");
      Collection<String> roles=ra==null ? List.of() : (Collection<String>) ra.getOrDefault("roles", List.of());
      return roles.stream().map(r -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_"+r)).collect(Collectors.toList());
    });
    return new ReactiveJwtAuthenticationConverterAdapter(c);
  }
}
