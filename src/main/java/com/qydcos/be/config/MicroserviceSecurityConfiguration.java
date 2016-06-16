package com.qydcos.be.config;



  import com.qydcos.be.security.AuthoritiesConstants;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
  import org.springframework.security.config.annotation.web.builders.HttpSecurity;
  import org.springframework.security.config.http.SessionCreationPolicy;
  import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
  import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
  import org.springframework.security.oauth2.provider.token.TokenStore;
  import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
  import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

  import javax.inject.Inject;

  @Configuration
  @EnableResourceServer
  @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
  public class MicroserviceSecurityConfiguration extends ResourceServerConfigurerAdapter {

      @Inject
      JHipsterProperties jHipsterProperties;


      @Override
      public void configure(HttpSecurity http) throws Exception {
          http
              .csrf()
              .disable()
              .headers()
              .frameOptions()
              .disable()
          .and()
              .sessionManagement()
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
              .authorizeRequests()
              .antMatchers("/api/**").authenticated()
              .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
              .antMatchers("/configuration/ui").permitAll();

      }

      @Bean
      public TokenStore tokenStore() {
          return new JwtTokenStore(jwtAccessTokenConverter());
      }

      @Bean
      public JwtAccessTokenConverter jwtAccessTokenConverter() {
          JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
          converter.setSigningKey(jHipsterProperties.getSecurity().getAuthentication().getJwt().getSecret());

          return converter;
      }
  }

