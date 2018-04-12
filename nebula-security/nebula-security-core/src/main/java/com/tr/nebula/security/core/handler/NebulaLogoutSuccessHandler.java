package com.tr.nebula.security.core.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by  Mustafa Erbin on 13.03.2017.
 */
public class NebulaLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Handle logout success and send logout message to client
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON + ";charset=" + java.nio.charset.StandardCharsets.UTF_8);
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, String> messageMap = new HashMap<String, String>();
        messageMap.put("message", "Successfully logged out");
        PrintWriter writer = response.getWriter();
        mapper.writeValue(writer, messageMap);
        response.getWriter().flush();
    }
}
