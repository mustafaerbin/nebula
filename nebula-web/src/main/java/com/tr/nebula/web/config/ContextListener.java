package com.tr.nebula.web.config;

import com.tr.nebula.common.converter.PropertySource;
import com.tr.nebula.common.util.Strings;
import com.tr.nebula.core.bundle.BundleBean;
import com.tr.nebula.core.bundle.BundleBeanListener;
import com.tr.nebula.core.bundle.BundleContext;
import com.tr.nebula.core.bundle.BundleStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.StandardServletEnvironment;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import java.io.InputStream;
import java.util.EventListener;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by kamilbukum on 09/03/2017.
 */
@Configuration
@ConfigurationProperties
public class ContextListener extends ServletRegistrationBean implements ServletContextListener, EventListener {

    @Autowired
    AutowireCapableBeanFactory beanFactory;

    @Autowired
    StandardServletEnvironment environment;

    @Autowired
    ConfigurableApplicationContext context;

    private BundleStarter config = new BundleStarter();
    private BundleContext<ServletContext> bundleContext;
    private Map<String, Object> nebula = new LinkedHashMap<>();

    public static String getBeanName(BundleBean bean) {
        String name = bean.getName();
        if (bean.getInstance().getClass().getSimpleName().equals(name)) {
            name = null;
        } else if (!Strings.has(bean.getName())) {
            name = Strings.unCapitalizeFirstChar(bean.getInstance().getClass().getSimpleName());
        }
        return name;
    }

    public Map<String, Object> getRobe() {
        return nebula;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        bundleContext = new BundleContext<>(servletContext, new BundleBeanListener() {
            @Override
            public void onCreate(BundleBean bean) {
                String name = getBeanName(bean);
                if (name != null) {
                    context.getBeanFactory().registerSingleton(name, bean.getInstance());
                } else {
                    context.getBeanFactory().autowireBean(bean.getInstance());
                }

            }

            public void destroy(BundleBean bean) {
                String name = getBeanName(bean);
                if (name != null) {
                    context.getBeanFactory().destroyBean(name, bean.getInstance());
                } else {
                    context.getBeanFactory().destroyBean(bean.getInstance());
                }
            }
        });

        PropertySource<InputStream> applicationConfig = ConfigurationFinder.getApplicationConfig(environment);
        config.onStart(applicationConfig, bundleContext, null);
        servletContext.addListener(this);
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        config.onStop(bundleContext);
    }
}

