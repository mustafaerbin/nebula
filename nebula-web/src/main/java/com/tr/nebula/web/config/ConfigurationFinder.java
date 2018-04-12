package com.tr.nebula.web.config;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.env.PropertySource;
import org.springframework.web.context.support.StandardServletEnvironment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kamilbukum on 10/03/2017.
 */

public class ConfigurationFinder {
    private static final String APP_CONFIG_EXTERNAL_REGEX = "applicationConfig\\: \\[file\\:(.*)\\]";
    private static final String APP_CONFIG_CLASSPATH_REGEX = "applicationConfig\\: \\[classpath\\:(.*)\\]";
    private static final Pattern APP_CONFIG_EXTERNAL_PATTERN = Pattern.compile(APP_CONFIG_EXTERNAL_REGEX);
    private static final Pattern APP_CONFIG_CLASSPATH_PATTERN = Pattern.compile(APP_CONFIG_CLASSPATH_REGEX);

    public static com.tr.nebula.common.converter.PropertySource getApplicationConfig(StandardServletEnvironment environment) {
        Iterator<PropertySource<?>> it = environment.getPropertySources().iterator();
        String sourcePath = null;
        InputStream is = null;
        while (it.hasNext()) {
            PropertySource<?> lSource = it.next();
            Matcher m = APP_CONFIG_EXTERNAL_PATTERN.matcher(lSource.getName());
            if(m.find()) {
                sourcePath = m.group(1);
                if(sourcePath != null) {
                    try {
                        is = new FileInputStream(sourcePath);
                    }catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
            } else {
                m = APP_CONFIG_CLASSPATH_PATTERN.matcher(lSource.getName());
                if(m.find()) {
                    String lSourcePath = m.group(1);
                    if(lSourcePath != null) {
                        sourcePath = lSourcePath;
                    }
                }
            }
        }

        if(is == null) {
            if(sourcePath != null) {
                is = ConfigurationFinder.class.getResourceAsStream(sourcePath);
            }
        }

        if(is != null) {
            com.tr.nebula.common.converter.PropertySource.Type type = com.tr.nebula.common.converter.PropertySource.Type.valueOf(
                    FilenameUtils.getExtension(sourcePath)
            );
            return new com.tr.nebula.common.converter.PropertySource(type, is);
        }

        return null;
    }
}
