package com.tr.nebula.persistence.api.criteria.cache;

/**
 * Created by kamilbukum on 12/01/2017.
 */
public interface EntityMetaFinder {
    EntityMeta getEntityMeta(Class<?> entityClass);
}
