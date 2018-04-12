package com.tr.nebula.quartz;

import com.google.common.collect.Sets;
import com.tr.nebula.core.bundle.BundleBean;
import com.tr.nebula.core.bundle.BundleContext;
import com.tr.nebula.quartz.info.JobInfo;
import com.tr.nebula.quartz.info.JobInfoProvider;
import com.tr.nebula.quartz.info.TriggerInfo;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class QuartzManaged {

    public static final ConcurrentHashMap<String, JobInfo> JOBS = new ConcurrentHashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzManaged.class);
    private Set<JobDetail> onStartJobs = new HashSet<>();
    private Set<JobDetail> onStopJobs = new HashSet<>();

    public QuartzManaged(QuartzConfiguration quartz, BundleContext<Object> context) {

        if (quartz != null) {
            try {
                initializeScheduler(quartz.getProperties());
                collectAndScheduleJobs(quartz.getScanPackages(), context);
            } catch (SchedulerException e) {
                LOGGER.error("SchedulerException:", e);
            }
        } else {
            LOGGER.warn("Quartz Configuration is empty");
        }

    }

    private void initializeScheduler(Properties properties) throws SchedulerException {
        SchedulerFactory factory = new StdSchedulerFactory(properties);
        Scheduler scheduler = factory.getScheduler();
        scheduler.start();
        JobManager.initialize(scheduler);
    }

    private void collectAndScheduleJobs(String[] packages, final BundleContext<Object> context) throws SchedulerException {
        Set<Class<? extends Job>> quartzJobs;

        onStartJobs.clear();
        onStopJobs.clear();

        JobManager.getInstance().getScheduler().getListenerManager().addJobListener(new JobListener() {
            @Override
            public String getName() {
                return "NebulaQuartzJobListener";
            }

            @Override
            public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
                context.getListener().onCreate(new BundleBean(jobExecutionContext.getJobDetail().getKey().getName(), jobExecutionContext.getJobInstance()));
            }

            @Override
            public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {

            }

            @Override
            public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
                context.getListener().destroy(new BundleBean(jobExecutionContext.getJobDetail().getKey().getName(), jobExecutionContext.getJobInstance()));
            }
        });

        for (String pkg : packages) {
            LOGGER.info("Scanning Jobs package : " + pkg);
            Reflections reflections = new Reflections(pkg);
            quartzJobs = reflections.getSubTypesOf(Job.class);


            for (Class<? extends Job> clazz : quartzJobs) {
                NebulaJob infoAnn = clazz.getDeclaredAnnotation(NebulaJob.class);
                if (infoAnn == null)
                    continue;

                JobInfoProvider infoProvider;
                try {
                    infoProvider = infoAnn.provider().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                Set<Trigger> triggers = new HashSet<>();

                JobInfo info = infoProvider.getJob(clazz);
                if (info == null || JOBS.containsKey(info.getJobClass().getName()))
                    continue;


                JobDetail detail = JobInfoProvider.convert2JobDetail(info);

                StringBuilder logBuilder = new StringBuilder();
                //Collect all triggers
                for (TriggerInfo tInfo : info.getTriggers()) {
                    logBuilder.append("\n\t\tTrigger: ")
                            .append(clazz.getName())
                            .append("\tName: ")
                            .append(tInfo.getName())
                            .append("\tType:")
                            .append(tInfo.getType().name());
                    switch (tInfo.getType()) {
                        case ON_APP_START:
                            onStartJobs.add(detail);
                            break;
                        case ON_APP_STOP:
                            onStopJobs.add(detail);
                            break;
                        default:
                            triggers.add(JobInfoProvider.convert2Trigger(tInfo, info));
                    }
                }
                LOGGER.info("\n\tClass {} \n\tName: {} \n\tDesc: {} \n\t Triggers: {}",
                        info.getJobClass().getName(),
                        info.getName(),
                        info.getDescription(),
                        logBuilder.toString()
                );
                JobManager.getInstance().scheduleJob(detail, triggers, true);
                JOBS.put(info.getJobClass().getName(), info);
            }
        }
    }


    public void start() throws SchedulerException {
        scheduleAllJobsOnApplicationStart();
    }

    public void stop() throws SchedulerException {
        scheduleAllJobsOnApplicationStop();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            LOGGER.info("Finished onStop Jobs Shutting down the application.");
        }
        if (JobManager.getInstance() != null)
            JobManager.getInstance().shutdown(true);
    }

    private void scheduleAllJobsOnApplicationStop() throws SchedulerException {
        for (JobDetail detail : onStopJobs) {
            if (!JobManager.getInstance().checkExists(detail.getKey()))
                JobManager.getInstance().scheduleJob(detail, executeNowTrigger());
            else
                JobManager.getInstance().scheduleJob(detail, addNowTrigger(detail.getKey()), true);

        }
    }


    private void scheduleAllJobsOnApplicationStart() throws SchedulerException {
        LOGGER.info("Jobs to run on application start: " + onStartJobs);
        for (JobDetail detail : onStartJobs) {
            if (!JobManager.getInstance().checkExists(detail.getKey()))
                JobManager.getInstance().scheduleJob(detail, executeNowTrigger());
            else
                JobManager.getInstance().scheduleJob(detail, addNowTrigger(detail.getKey()), true);
        }
    }

    private Trigger executeNowTrigger() {
        return TriggerBuilder.newTrigger().startNow().build();
    }

    private Set<Trigger> addNowTrigger(JobKey key) throws SchedulerException {
        Set<Trigger> triggers = Sets.newHashSet(JobManager.getInstance().getTriggersOfJob(key.getName(), key.getGroup()));
        triggers.add(executeNowTrigger());
        return triggers;
    }

}
