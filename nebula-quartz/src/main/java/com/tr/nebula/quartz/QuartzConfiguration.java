package com.tr.nebula.quartz;

import java.util.Arrays;
import java.util.Properties;

/**
 * Created by Mustafa Erbin on 07/03/2017.
 */
public class QuartzConfiguration {
    private String[] scanPackages;
    private Properties properties;

    public String[] getScanPackages() {
        return scanPackages;
    }

    public void setScanPackages(String[] scanPackages) {
        this.scanPackages = Arrays.copyOf(scanPackages, scanPackages.length);
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }


}
