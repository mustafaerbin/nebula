package com.tr.nebula.security.web.handler;

import com.tr.nebula.security.api.model.SessionUser;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by Mustafa Erbin on 2.05.2017.
 */
public class NebulaSessionUserHandler implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(SessionUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        return getSessionUser();
    }

    private SessionUser getSessionUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null)
            return (SessionUser) authentication.getPrincipal();

        return null;
    }
}
