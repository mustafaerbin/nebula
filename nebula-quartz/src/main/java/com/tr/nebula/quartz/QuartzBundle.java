package com.tr.nebula.quartz;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tr.nebula.core.bundle.Bundle;
import com.tr.nebula.core.bundle.BundleConfig;
import com.tr.nebula.core.bundle.BundleContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hasanmumin on 10/03/2017.
 */
@BundleConfig(order = 2)
public class QuartzBundle extends Bundle<QuartzConfiguration, Object> {

    private static final TypeReference<QuartzConfiguration> TYPE_REFERENCE = new TypeReference<QuartzConfiguration>() {
    };

    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzBundle.class);

    private QuartzManaged quartzManaged = null;


    @Override
    public String getPropertyName() {
        return "nebula.quartz";
    }

    @Override
    public TypeReference<QuartzConfiguration> getTypeReference() {
        return TYPE_REFERENCE;
    }

    @Override
    public void onStart(QuartzConfiguration configuration, BundleContext<Object> context) {
        quartzManaged = new QuartzManaged(configuration, context);
        try {
            quartzManaged.start();
        } catch (SchedulerException e) {
            LOGGER.error("Error when quartz starting... ", e);
            e.printStackTrace();
        }
    }

    @Override
    public void onStop(BundleContext<Object> context) {
        try {
            quartzManaged.stop();
        } catch (SchedulerException e) {
            LOGGER.error("Error when quartz stopping... ", e);
        }
    }
}
