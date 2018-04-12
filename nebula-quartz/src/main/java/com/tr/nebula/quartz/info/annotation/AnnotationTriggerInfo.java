package com.tr.nebula.quartz.info.annotation;

import com.tr.nebula.quartz.NebulaTrigger;
import com.tr.nebula.quartz.info.TriggerInfo;

class AnnotationTriggerInfo implements TriggerInfo {
    private String name;
    private String group;
    private long startTime;
    private long endTime;
    private int repeatCount;
    private long repeatInterval;
    private String cron;
    private TriggerInfo.Type type;

    public AnnotationTriggerInfo(NebulaTrigger ann) {
        name = ann.name();
        group = ann.group();
        startTime = ann.startTime();
        endTime = ann.endTime();
        repeatCount = ann.repeatCount();
        repeatInterval = ann.repeatInterval();
        cron = ann.cron();
        type = ann.type();

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public long getEndTime() {
        return endTime;
    }

    @Override
    public int getRepeatCount() {
        return repeatCount;
    }

    @Override
    public long getRepeatInterval() {
        return repeatInterval;
    }

    @Override
    public String getCron() {
        return cron;
    }

    @Override
    public Type getType() {
        return type;
    }
}
