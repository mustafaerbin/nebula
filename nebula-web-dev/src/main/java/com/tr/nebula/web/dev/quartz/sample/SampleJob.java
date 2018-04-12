package com.tr.nebula.web.dev.quartz.sample;

import com.tr.nebula.quartz.NebulaJob;
import com.tr.nebula.quartz.NebulaTrigger;
import com.tr.nebula.quartz.info.TriggerInfo;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

@NebulaJob(name = "SampleJob", description = "Sample Quartz Job for a demonstration.", triggers = {
        @NebulaTrigger(cron = "0/4 * * * * ?", name = "Every 4 seconds", group = "Sample", type = TriggerInfo.Type.CRON),
        @NebulaTrigger(cron = "0/6 * * * * ?", name = "Every 6 seconds", group = "Sample", type = TriggerInfo.Type.CRON),
        @NebulaTrigger(name = "On App Start", group = "Sample", type = TriggerInfo.Type.ON_APP_START),
        @NebulaTrigger(name = "On App Stop", group = "Sample", type = TriggerInfo.Type.ON_APP_STOP)
})
public class SampleJob implements Job {
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleJob.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("JOb running NextFireTime: {},ApplicationContext: {}", jobExecutionContext.getNextFireTime(), applicationContext);

    }
}
