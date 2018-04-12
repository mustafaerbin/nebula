package com.tr.nebula.security.core.annotation;

import java.lang.annotation.*;

/**
 * Created by Mustafa Erbin on 23.03.2017.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Auth {
}
