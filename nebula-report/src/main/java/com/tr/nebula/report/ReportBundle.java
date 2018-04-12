package com.tr.nebula.report;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tr.nebula.core.bundle.Bundle;
import com.tr.nebula.core.bundle.BundleContext;
import org.jfree.report.JFreeReportBoot;
import org.springframework.stereotype.Service;

/**
 * Created by Mustafa Erbin on 18.05.2017.
 */
@Service
public class ReportBundle extends Bundle<ReportConfiguration, Object> {
    private static final TypeReference<ReportConfiguration> TYPE_REFERENCE = new TypeReference<ReportConfiguration>() {
    };


   private ReportConfiguration reportConfiguration;

    @Override
    public String getPropertyName() {
        return "nebula.report";
    }

    @Override
    public TypeReference<ReportConfiguration> getTypeReference() {
        return TYPE_REFERENCE;
    }

    @Override
    public void onStart(ReportConfiguration configuration, BundleContext<Object> context) {
        //TODO
        setReportConfiguration(configuration);
        JFreeReportBoot jFreeReportBoot;
        jFreeReportBoot=JFreeReportBoot.getInstance();
        jFreeReportBoot.start();
        ReportUtil.setInputPath(configuration.getInputPath());
        ReportUtil.setOutputPath(configuration.getOutputPath());
    }

    @Override
    public void onStop(BundleContext<Object> context) {
        //TODO

    }

    public ReportConfiguration getReportConfiguration() {
        return reportConfiguration;
    }

    public void setReportConfiguration(ReportConfiguration reportConfiguration) {
        this.reportConfiguration = reportConfiguration;
    }
}
