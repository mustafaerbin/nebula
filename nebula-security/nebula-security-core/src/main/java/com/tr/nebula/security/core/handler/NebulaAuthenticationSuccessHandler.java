package com.tr.nebula.security.core.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by  Mustafa Erbin on 11.03.2017.
 */
public class NebulaAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    /**
     *
     */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Handle authorization success and return current user data to client
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON + ";charset=" + java.nio.charset.StandardCharsets.UTF_8);
        PrintWriter writer = response.getWriter();
        mapper.writeValue(writer, authentication.getPrincipal());
        response.getWriter().flush();
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
