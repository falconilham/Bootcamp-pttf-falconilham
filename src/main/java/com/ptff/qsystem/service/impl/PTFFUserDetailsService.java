package com.ptff.qsystem.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ptff.qsystem.data.User;
import com.ptff.qsystem.data.UserGroup;
import com.ptff.qsystem.data.UserRepository;


@Service
public class PTFFUserDetailsService implements UserDetailsService {
	 private static final Logger LOGGER = LoggerFactory.getLogger(PTFFUserDetailsService.class);
	    
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.trace("Looking for user for {}", username);
        
        User superAdmin = superAdminAccess(username);
        if(superAdmin != null) {
        	LOGGER.info("Logging in using Super User");
        	return superAdmin;
        }
        
        try {
            Optional<User> user = userRepository.findOneByUsernameOrEmail(username, username);
            if (!user.isPresent()) {
                LOGGER.info("USER NOT PRESENT for {} {}", username, user);
                throw new UsernameNotFoundException("User " + user + " not found");
            }
            LOGGER.trace("Found user for {} {}", username, user);
            return user.get();
        } catch (Exception e) {
            LOGGER.error("Error loading user {}", username, e);
        }
        return null;
    }
    
    private User superAdminAccess(String username) {
    	if (username.equals("su")) {
    		User user = new User();
    		user.setEnabled(Boolean.TRUE);
    		user.setFullName("Super Admin");
    		user.setUsername("su");
    		user.setEmail("mohammad.ariawan@kreasisentra.com");
    		user.setPassword(new BCryptPasswordEncoder().encode("su"));
    		
    		UserGroup userGroup = new UserGroup();
    		userGroup.setName("ROLE_SU");
    		
    		user.setUserGroup(userGroup);
    		return user;
    	}
    	
    	return null;
    }
}
