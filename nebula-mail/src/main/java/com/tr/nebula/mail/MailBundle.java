package com.tr.nebula.mail;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tr.nebula.common.util.Strings;
import com.tr.nebula.core.bundle.Bundle;
import com.tr.nebula.core.bundle.BundleBean;
import com.tr.nebula.core.bundle.BundleContext;
import com.tr.nebula.mail.sender.NebulaMailSender;
import com.tr.nebula.mail.sender.NebulaMailSenderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * Bundle class for mail implementation.
 */
public class MailBundle extends Bundle<Map<String, MailConfiguration>, Object> {
    private static final TypeReference<Map<String, MailConfiguration>> CONFIGURATION_TYPE_REFERENCE = new TypeReference<Map<String, MailConfiguration>>() {
    };
    private static final Logger LOGGER = LoggerFactory.getLogger(MailBundle.class);


    @Override
    public String getPropertyName() {
        return "nebula.mail";
    }


    @Override
    public TypeReference<Map<String, MailConfiguration>> getTypeReference() {
        return CONFIGURATION_TYPE_REFERENCE;
    }

    @Override
    public void onStart(Map<String, MailConfiguration> configurations, BundleContext<Object> context) {
        if (configurations != null) {
            for (Map.Entry<String, MailConfiguration> entry : configurations.entrySet()) {
                NebulaMailSender sender = new NebulaMailSenderImpl(entry.getValue());
                if (entry.getKey() == null) {
                    context.getListener().onCreate(new BundleBean(Strings.unCapitalizeFirstChar(sender.getClass().getSimpleName()), sender));
                } else {
                    context.getListener().onCreate(new BundleBean(entry.getKey(), sender));
                }
            }
        } else {
            LOGGER.warn("Bundle included but no configuration (mail) found at yml.");
        }
    }
}
