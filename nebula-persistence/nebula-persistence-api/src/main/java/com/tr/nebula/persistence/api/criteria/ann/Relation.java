package com.tr.nebula.persistence.api.criteria.ann;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mustafa Erbin on 20/01/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Relation {
    String name();
}