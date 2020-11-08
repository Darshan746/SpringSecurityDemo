package com.example.demo.security;

import com.example.demo.auth.ApplicationUserService;
import com.example.demo.jwt.JwtTokenVerifier;
import com.example.demo.jwt.JwtUserNameAndPasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    ApplicationSecurityConfig(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {



        //For Form based and Role based Authentication

       /* http
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
                .logoutSuccessUrl("/login");*/


     //  NOTE  // for JWT token Authentication

        http.
                csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUserNameAndPasswordAuthenticationFilter(authenticationManager()))
                .addFilterAfter(new JwtTokenVerifier(),JwtUserNameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/","/index", "/css/*","js/*").permitAll()
                .anyRequest().authenticated();
    }


    /**
     * Commented this code because we have implemented our own UserDetail Service in Service Layer
     */
   /* @Override
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

    }*/


    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
}

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }
}
