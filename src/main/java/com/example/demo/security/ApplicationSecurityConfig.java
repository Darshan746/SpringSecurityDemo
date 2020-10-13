package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private PasswordEncoder passwordEncoder;

    @Autowired
    ApplicationSecurityConfig(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/","/css","/js").permitAll()  //antMatchers -> It will not apply security to the mentioned URL //permitAll-> Tells that dont apply the security to the above mentioned URL.
                //.antMatchers("/api/**").hasAnyRole(ApplicationUserRole.STUDENT.name())
               // .antMatchers(HttpMethod.POST,"/management/api/v1/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
               // .antMatchers(HttpMethod.GET, "/management/api/v1/**").hasAnyRole(ApplicationUserRole.ADMIN.name(), ApplicationUserRole.ADMINTRAINEE.name())
                .anyRequest()
                .authenticated()
                .and()
               // .httpBasic();
                .formLogin()
                .loginPage("/login")
                .permitAll()
        .defaultSuccessUrl("/courses", true)
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .logoutSuccessUrl("/login");
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails userDetailsServiceRama =  User.builder()
                .username("Rama")
                .password(passwordEncoder.encode("password"))
                //.roles(ApplicationUserRole.STUDENT.name()) //Internally it will become ROLE_STUDENT
                .authorities(ApplicationUserRole.STUDENT.getGrantedAuthorities())
                .build();

        UserDetails userDetailsServiceKrishna =  User.builder()
                .username("Krishna")
                .password(passwordEncoder.encode("password123"))
                //.roles(ApplicationUserRole.ADMIN.name()) //Internally it will become ROLE_ADMIN
                .authorities(ApplicationUserRole.ADMIN.getGrantedAuthorities())
                .build();

        UserDetails userDetailsServiceLinda =  User.builder()
                .username("Linda")
                .password(passwordEncoder.encode("password123"))
               // .roles(ApplicationUserRole.ADMINTRAINEE.name()) //Internally it will become ROLE_ADMIN
                .authorities(ApplicationUserRole.ADMINTRAINEE.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(userDetailsServiceRama,
                userDetailsServiceKrishna,
                userDetailsServiceLinda);
    }


    public static void main(String[] args) {

    }
}
