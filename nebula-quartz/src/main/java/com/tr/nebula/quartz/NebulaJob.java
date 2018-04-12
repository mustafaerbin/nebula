package com.tr.nebula.quartz;

import com.tr.nebula.quartz.info.JobInfoProvider;
import com.tr.nebula.quartz.info.annotation.AnnotationJobInfoProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface NebulaJob {

    String name();

    String group() default "DEFAULT";

    String description();

    NebulaTrigger[] triggers() default {};

    Class<? extends JobInfoProvider> provider() default AnnotationJobInfoProvider.class;
}
