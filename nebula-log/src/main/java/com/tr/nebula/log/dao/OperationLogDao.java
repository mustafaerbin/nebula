package com.tr.nebula.log.dao;

import com.tr.nebula.log.entity.OperationLog;
import com.tr.nebula.persistence.jpa.dao.BaseDaoImpl;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;


/**
 * Created by Ali Kızılırmak on 16.05.2017.
 */
@Service
public class OperationLogDao extends BaseDaoImpl<OperationLog, Long> {
    public OperationLogDao() {
    }

    public OperationLogDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
