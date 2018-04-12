package com.tr.nebula.security.db.service;

import com.tr.nebula.persistence.jpa.services.JpaService;
import com.tr.nebula.security.api.domain.NebulaUser;
import com.tr.nebula.security.api.model.SessionUser;
import com.tr.nebula.security.db.repository.NebulaUserRepository;
import com.tr.nebula.security.api.service.NebulaUserService;
import com.tr.nebula.security.db.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Mustafa Erbin on 21.03.2017.
 */
@Service
public class UserService extends JpaService<User, Long> implements NebulaUserService {

    NebulaUserRepository repository;

    @Autowired
    public UserService(NebulaUserRepository repository) {
        super(repository);
        this.repository = repository;
    }

    /**
     * Find user by username and match passwords(db password and request password)
     * @param username
     * @param password
     * @return
     */
    @Override
    public Optional<? extends NebulaUser> getUserByUsernameAndPassword(String username, String password) {
        Optional<User> user = repository.findOneByUsername(username);
        if(user.isPresent()){
            User userObj = (User)user.get();
            if(userObj.getPassword().equals(password)){
                return (Optional<? extends NebulaUser>)user;
            }
        }
        return null;
    }

    public SessionUser getSessionUser(){
        SessionUser user = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            user = (SessionUser) auth.getPrincipal();
        }

        return user;
    }
}
