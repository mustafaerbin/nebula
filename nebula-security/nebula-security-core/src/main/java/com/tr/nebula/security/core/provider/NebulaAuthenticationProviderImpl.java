package com.tr.nebula.security.core.provider;

import com.tr.nebula.security.api.domain.NebulaUser;
import com.tr.nebula.security.api.ldap.NebulaLdapService;
import com.tr.nebula.security.api.model.SessionUser;
import com.tr.nebula.security.api.provider.NebulaAuthenticationProvider;
import com.tr.nebula.security.api.service.NebulaUserService;
import com.tr.nebula.security.core.cache.SecurityCache;
import com.tr.nebula.security.core.model.SessionUserImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * Created by Mustafa Erbin on 21.03.2017.
 */
@Service
public class NebulaAuthenticationProviderImpl implements NebulaAuthenticationProvider {

    @Autowired
    private NebulaUserService nebulaUserService;

    @Autowired
    private NebulaLdapService nebulaLdapService;

    /**
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    /**
     * Custom authentication
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(authentication.getPrincipal() instanceof SessionUser) // if user already logged in
            return authentication;

        String authType = (String) SecurityCache.get("authType");

        String userName = (String)authentication.getPrincipal(); // username
        String password = (String)authentication.getCredentials(); // password
        NebulaUser user = null;
        if(authType.equals("DB")) {
            user = nebulaUserService.getUserByUsernameAndPassword(userName, password).get(); // match user using username and password
        }else if(authType.equals("LDAP"))
            user = nebulaLdapService.getUser(userName, password);

        if(user != null) {// user is present
            SessionUserImpl currentUser = new SessionUserImpl(user); // create current user data using user entity
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(currentUser, password,currentUser.getAuthorities()); // create token for logged in user
            return token;
        } else { // user is not present
            throw new InternalAuthenticationServiceException("Invalid Username or Password");
        }
    }
}
