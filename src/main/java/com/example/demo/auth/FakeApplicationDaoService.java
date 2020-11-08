package com.example.demo.auth;

import com.example.demo.security.ApplicationUserRole;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("fake")
public class FakeApplicationDaoService implements  ApplicationUserDAO {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public FakeApplicationDaoService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUserName(String userName) {
        return getApplicationUser().stream().filter(applicationUser -> applicationUser.getUsername().equals(userName)).findFirst();

//        for(ApplicationUser applicationUser: getApplicationUser()){
//            if(applicationUser.getUsername().equals(userName)){
//                return Optional.ofNullable(applicationUser);
//            }
//        }
//        return Optional.empty();
//    }
    }

    private List<ApplicationUser> getApplicationUser() {

       return Lists.newArrayList(new ApplicationUser(ApplicationUserRole.STUDENT.getGrantedAuthorities()
                        , passwordEncoder.encode("password")
                        , "Rama"
                        , true
                        , true
                        , true
                        , true
               ,true),
                new ApplicationUser(ApplicationUserRole.ADMIN.getGrantedAuthorities()
                        , passwordEncoder.encode("password")
                        , "Krishna"
                        , true
                        , true
                        , true
                        , true,true)
                , new ApplicationUser(ApplicationUserRole.ADMINTRAINEE.getGrantedAuthorities()
                        , passwordEncoder.encode("password")
                        , "LINDA"
                        , true
                        , true
                        , true
                        , true,true));
    }
}

