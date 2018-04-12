package com.tr.nebula.security.core.entryPoint;

import com.tr.nebula.security.api.entryPoint.NebulaAuthenticationEntryPoint;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by  Mustafa Erbin on 21.03.2017.
 */
@Component("authenticationEntryPoint")
public class NebulaAuthenticationEntryPointImpl implements NebulaAuthenticationEntryPoint {
    /**
     * Authentication endpoint
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
