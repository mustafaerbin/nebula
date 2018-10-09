package com.tr.nebula.persistence.jpa.services;

import com.tr.nebula.common.exception.ErrorCode;
import com.tr.nebula.common.exception.NebulaCommonException;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.io.Serializable;

/**
 * Created by Mustafa Erbin on 01/03/2017.
 */
public class CrudService<T, ID extends Serializable> {

    protected CrudRepository<T, ID> crudRepository;

    public CrudService(CrudRepository<T, ID> crudRepository){
        this.crudRepository = crudRepository;
    }

    public T findOne(ID id) {
        return crudRepository.findOne(id);
    }

    @Transactional
    public T create(T entity){
        return crudRepository.save(entity);
    }

    @Transactional
    public T update(T entity, ID id){
        if(!exists(id)) {
            throw new NebulaCommonException(ErrorCode.HTTP_404, "Upgrading data not found ! ");
        }
        return crudRepository.save(entity);
    }

    @Transactional
    public T delete(ID id) {
        T entity = findOne(id);
        if(entity == null) {
            throw new NebulaCommonException(ErrorCode.HTTP_404, "Deleting data not found ! ");
        }
        crudRepository.delete(id);
        return entity;
    }

    @Transactional
    public T delete(T t) {
        crudRepository.delete(t);
        return t;
    }

    @Transactional
    public Iterable<? extends T> delete(Iterable<? extends T> entities) {
        crudRepository.delete(entities);
        return entities;
    }

    @Transactional
    public void deleteAll() {
        crudRepository.deleteAll();
    }

    public boolean exists(ID id) {
        return crudRepository.exists(id);
    }

    public Iterable<T> findAll() {
        return crudRepository.findAll();
    }

    public Iterable<T> findAll(Iterable<ID> iterable) {
        return crudRepository.findAll(iterable);
    }

    public long count() {
        return crudRepository.count();
    }
}
