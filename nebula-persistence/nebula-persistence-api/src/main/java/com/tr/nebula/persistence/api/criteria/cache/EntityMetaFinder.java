package com.tr.nebula.persistence.api.criteria.cache;

/**
 * Created by Mustafa Erbin on 12/01/2017.
 */
public interface EntityMetaFinder {
    EntityMeta getEntityMeta(Class<?> entityClass);
}
