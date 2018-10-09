package com.tr.nebula.quartz.info;

/**
 * Created by Mustafa Erbin on 06/03/2017.
 */
public interface TriggerInfo {

    String getName();

    String getGroup();

    long getStartTime();

    long getEndTime();

    int getRepeatCount();

    long getRepeatInterval();

    String getCron();

    Type getType();

    enum Type {
        SIMPLE,
        CRON,
        ON_APP_START,
        ON_APP_STOP
    }
}
