package com.tr.nebula.security.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tr.nebula.security.core.annotation.Auth;
import com.tr.nebula.security.core.entryPoint.NebulaAuthenticationEntryPointImpl;
import com.tr.nebula.security.core.filter.NebulaUsernamePasswordAuthenticationFilter;
import com.tr.nebula.security.core.handler.NebulaAuthenticationFailureHandler;
import com.tr.nebula.security.core.handler.NebulaAuthenticationSuccessHandler;
import com.tr.nebula.security.core.handler.NebulaLogoutSuccessHandler;
import com.tr.nebula.security.core.permission.NebulaAccessFilter;
import com.tr.nebula.security.core.provider.NebulaAuthenticationProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by  Mustafa Erbin on 21.03.2017.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class NebulaSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private NebulaAuthenticationProviderImpl autheticationProvider;

    @Autowired
    private NebulaAuthenticationEntryPointImpl authenticationEntryPoint;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    private static String restPath;

    private static String[] permittedPaths;

    /**
     * HttpSecurity configuration
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(permittedPaths).permitAll()
                .antMatchers(getAuthenticationPaths()).authenticated()
                //.anyRequest().fullyAuthenticated()
                .and()
                .formLogin()
                .loginPage(getRestPath() + "/login")
                .permitAll()
                .and()
                .logout()
                .logoutUrl(getRestPath() + "/logout")
                .logoutSuccessHandler(new NebulaLogoutSuccessHandler())
                .deleteCookies("remember-me", "JSESSIONID")
                .permitAll()
                .and()
                .rememberMe()
                .and()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .addFilterBefore(getNebulaUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(getNebulaAccessFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * AuthenticationManager configuration
     *
     * @param auth
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(autheticationProvider);
    }

    /**
     * Create CustomUsernamePaswordAuthenticationFilter
     *
     * @return
     * @throws Exception
     */
    public NebulaUsernamePasswordAuthenticationFilter getNebulaUsernamePasswordAuthenticationFilter() throws Exception {
        NebulaUsernamePasswordAuthenticationFilter filter = new NebulaUsernamePasswordAuthenticationFilter(new NebulaAuthenticationSuccessHandler(), new NebulaAuthenticationFailureHandler(), new ObjectMapper());
        filter.setAuthenticationManager(authenticationManager());
        filter.setSessionAuthenticationStrategy(getSessionAuthenticationStrategy());
        return filter;
    }

    /**
     * Create SessionAuthenticationStrategy
     *
     * @return
     */
    public SessionAuthenticationStrategy getSessionAuthenticationStrategy() {
        SessionRegistryImpl sessionRegistry = new SessionRegistryImpl();
        RegisterSessionAuthenticationStrategy registerSessionAuthenticationStrategy = new RegisterSessionAuthenticationStrategy(sessionRegistry);
        ChangeSessionIdAuthenticationStrategy strategy = new ChangeSessionIdAuthenticationStrategy();
        strategy.setApplicationEventPublisher(getApplicationContext());
        ArrayList list = new ArrayList();
        list.add(strategy);
        list.add(registerSessionAuthenticationStrategy);
        return new CompositeSessionAuthenticationStrategy(list);
    }

    @Bean
    public NebulaAccessFilter getNebulaAccessFilter(){
        return new NebulaAccessFilter();
    }

    public static String getRestPath() {
        return restPath;
    }

    public static void setRestPath(String restPath) {
        NebulaSecurityConfig.restPath = restPath;
    }

    public static String[] getPermittedPaths() {
        return permittedPaths;
    }

    public static void setPermittedPaths(String[] permittedPaths) {
        NebulaSecurityConfig.permittedPaths = permittedPaths;
    }

    private String[] getAuthenticationPaths(){
        List<String> authPaths = new ArrayList<>();
        Set<RequestMappingInfo> requestMappingInfos = requestMappingHandlerMapping.getHandlerMethods().keySet();
        for (RequestMappingInfo info : requestMappingInfos) {
            Method rMethod = requestMappingHandlerMapping.getHandlerMethods().get(info).getMethod();
            if (rMethod.getAnnotation(Auth.class) != null) {
                String path = (String) info.getPatternsCondition().getPatterns().toArray()[0];
                authPaths.add(getRestPath() + "/" + path);
            }
        }
        String[] authPathsArray = new String[authPaths.size()];
        authPathsArray = authPaths.toArray(authPathsArray);
        return authPathsArray;
    }
}
