package com.tr.nebula.quartz.info.annotation;

import com.tr.nebula.quartz.NebulaJob;
import com.tr.nebula.quartz.info.JobInfo;
import com.tr.nebula.quartz.info.JobInfoProvider;
import org.quartz.Job;

/**
 * A Util class that helps converting annotations to quartz objects.
 */
public class AnnotationJobInfoProvider extends JobInfoProvider {

    @Override
    public JobInfo getJob(Class<? extends Job> clazz) {
        NebulaJob jAnn = clazz.getAnnotation(NebulaJob.class);
        if (jAnn == null)
            return null;// TODO throw exception ?
        return new AnnotationJobInfo(jAnn, clazz);
    }
}
