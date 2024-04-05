package ru.skillsmart.fleet.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import ru.skillsmart.fleet.service.CustomAuthenticationSuccessHandler;
import ru.skillsmart.fleet.service.CustomUserDetailsService;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    PasswordEncoder passwordEncoder;

    @Autowired
    DataSource ds;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        //auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //на гитхаб есть коммит со вторым вариантом аут/автор. См. коммиты.
    //данный вариант - "JDBC-Аутентификация со своими таблицами"
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and().authorizeRequests()
        http.authorizeRequests()
                    .antMatchers("/css/**", "/js/**")
                    .permitAll()
                    .antMatchers("/api/**")
                    //.hasAnyRole("ADMIN", "USER")
                    .hasAnyAuthority("MANAGER", "ADMIN")
                    .anyRequest()
                    .authenticated()
                    //.permitAll()
//                .antMatchers(HttpMethod.GET).hasAnyAuthority("USER", "MANAGER")
//                .antMatchers(HttpMethod.POST).hasAuthority("MANAGER")
//                .antMatchers(HttpMethod.PUT).hasAuthority("MANAGER")
//                .antMatchers(HttpMethod.DELETE).hasAuthority("MANAGER")
                .and()
                .exceptionHandling().accessDeniedHandler((request, response, accessDeniedException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .formLogin()
                .loginPage("/login")
//                .defaultSuccessUrl("/")
                .successHandler(customAuthenticationSuccessHandler)
                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .permitAll()
                .and()
                .csrf()
                .disable();
    }

}
