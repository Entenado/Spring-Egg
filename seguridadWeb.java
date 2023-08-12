
package com.egg.biblioteca;

import com.egg.biblioteca.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class seguridadWeb  {
    
    @Autowired
    public UsuarioServicio usuarioservicio;
    
    @Autowired
    public void configureGlobal (AuthenticationManagerBuilder auth) throws Exception{
    
        auth.userDetailsService(usuarioservicio)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
    
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
          http
                  .authorizeHttpRequests()
                  .requestMatchers("/admin/*").hasRole("ADMIN")
                  .requestMatchers("/css/*","/js/*","/*","/img/*","/**")
                  .permitAll()
          .and().formLogin()
                  .loginPage("/login")
                  .loginProcessingUrl("/logincheck")
                  .usernameParameter("email")
                  .passwordParameter("password")
                  .defaultSuccessUrl("/inicio")
                  .permitAll()
          .and().logout()
                  .logoutUrl("/logout")
                  .logoutSuccessUrl("/")
                  .permitAll()
          .and().csrf()
                  .disable();
          
          
        return http.build();
               
    }
         
}  


