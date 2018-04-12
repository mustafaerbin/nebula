package com.tr.nebula.persistence.api.dao;

import com.tr.nebula.persistence.api.criteria.Criteria;
import com.tr.nebula.persistence.api.criteria.Result;
import com.tr.nebula.persistence.api.customutil.CustomQuery;
import com.tr.nebula.persistence.api.query.search.SearchModel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by kamilbukum on 20/03/2017.
 */
public interface BaseDao<T, ID extends Serializable> {
    /**
     * Returns modified list of the entities regarding to the search model.
     * {@inheritDoc}
     *
     * @return List of entities.
     */
    Criteria<T> queryAllStrict(SearchModel search);

    /**
     * Returns modified list of the entities regarding to the search model.
     * {@inheritDoc}
     *
     * @return List of entities.
     */
    Criteria<Map<String, Object>> queryAll(SearchModel search);

    /**
     * Returns modified list of the entities regarding to the search model.
     * {@inheritDoc}
     *
     * @return List of entities.
     */
    <E> Criteria<E> queryAll(SearchModel search, Class<E> transformClass);

    /**
     * Returns modified list of the entities regarding to the search model.
     * {@inheritDoc}
     *
     * @return List of entities.
     */
    Result<T> findAllStrict(SearchModel search);

    /**
     * Returns modified list of the entities regarding to the search model.
     * {@inheritDoc}
     *
     * @return List of entities.
     */
    Result<Map<String, Object>> findAll(SearchModel search);

    /**
     * Returns modified list of the entities regarding to the search model.
     * {@inheritDoc}
     *
     * @return List of entities.
     */
    <E> Result<E> findAll(SearchModel search, Class<E> transformClass);

    /**
     *
     * @return
     */
    List<T> findAll();
    /**
     * {@inheritDoc}
     *
     * @param oid id of the desired Entity
     * @return returns the result.
     */
    T findById(ID oid);

    /**
     * {@inheritDoc}
     *
     * @param oid id of the Given Entity
     * @return returns the result.
     */
    @SuppressWarnings("unchecked")
    <E extends Serializable> E findById(Class<E> clazz, Serializable oid);

    /**
     * Create a record for the given entity instance.
     *
     * @param entity to record.
     * @return updated version of the instance.
     */
    T create(T entity);

    /**
     * Update a record for the given entity instance.
     *
     * @param entity to record.
     * @return updated version of the instance.
     */
    T update(T entity);

    /**
     * Delete a record for the given entity instance.
     *
     * @param id to record.
     * @return updated version of the instance.
     */
    public T delete(ID id);
    /**
     * Delete a record for the given entity instance.
     *
     * @param entity to record.
     * @return updated version of the instance.
     */
    T delete(T entity);

    /**
     * Flush the session.
     */
    void flush();

    /**
     * Merges the entity with the session.
     *
     * @param entity entity to merge
     * @return
     */
    @SuppressWarnings("unchecked")
    T merge(T entity);

    /**
     * Detached the entity from session by evict method.
     *
     * @param entity entity to detach
     * @return
     */
    T detach(T entity);

    /**
     *
     * @return fields in the target entity
     */
    List getProperties();

    /**
     *
     * @param propertyClass
     * @return fields in the selected entity
     */
    List getProperties(Class propertyClass);

    /**
     *
     * @param propertyMap
     * @return
     */
    Result<T> findByPropertiesAsResult(Map<String, Object> propertyMap);

    /**
     *
     * @param propertyMap
     * @return
     */
    List<T> findByProperties(Map<String, Object> propertyMap);

    void truncate(String tableName);

    List findAll(CustomQuery query);

    Map getResults(CustomQuery queryObj);

}
