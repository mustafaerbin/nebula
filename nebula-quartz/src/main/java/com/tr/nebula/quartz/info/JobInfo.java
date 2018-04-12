package com.tr.nebula.quartz.info;

import org.quartz.Job;

import java.util.List;

public interface JobInfo {

    String getName();

    String getGroup();

    String getDescription();

    List<TriggerInfo> getTriggers();

    void setTriggers(List<TriggerInfo> triggers);

    Class<? extends Job> getJobClass();

    Class<? extends JobInfoProvider> getProvider();
}
