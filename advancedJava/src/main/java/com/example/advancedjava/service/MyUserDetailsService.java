package com.example.advancedjava.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.advancedjava.dao.EmployeeDao;
import com.example.advancedjava.helper.UserPrincipal;
import com.example.advancedjava.model.Employee;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    EmployeeDao userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee user=userRepo.findByEmpid(username);
        if(user==null) throw new UsernameNotFoundException("Not user found");
      

        return new UserPrincipal(user);

    }
}

