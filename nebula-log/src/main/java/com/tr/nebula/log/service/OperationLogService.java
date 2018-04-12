package com.tr.nebula.log.service;

import com.tr.nebula.log.dao.OperationLogDao;
import com.tr.nebula.log.entity.CrudType;
import com.tr.nebula.log.entity.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Ali Kızılırmak on 16.05.2017.
 */
@Service
public class OperationLogService {

    OperationLog log = null;

    @Autowired
    OperationLogDao logDao;

    public OperationLogService() {

    }

    public OperationLogService(OperationLogDao logDao) {
        this.logDao = logDao;
    }

    /**
     *
     * @param logMessage
     */
    public void saveLog(String logMessage){
        log = new OperationLog();
        log.setLogMessage(logMessage);
        logDao.create(log);
    }

    /**
     *
     * @param logMessage
     * @param crudType
     */
    public void saveLog(String logMessage, CrudType crudType){
        log = new OperationLog();
        log.setLogMessage(logMessage);
        log.setCrudType(crudType);
        logDao.create(log);
    }

    /**
     *
     * @param logMessage
     * @param crudType
     * @param taskID
     */
    public void saveLog(String logMessage, CrudType crudType, Long taskID){
        log = new OperationLog();
        log.setLogMessage(logMessage);
        log.setCrudType(crudType);
        log.setTaskID(taskID);
        logDao.create(log);
    }

    /**
     *
     * @param logMessage
     * @param crudType
     * @param taskID
     * @param policyID
     */
    public void saveLog(String logMessage, CrudType crudType, Long taskID, Long policyID){
        log = new OperationLog();
        log.setLogMessage(logMessage);
        log.setCrudType(crudType);
        log.setTaskID(taskID);
        log.setPolicyID(policyID);
        logDao.create(log);
    }

    /**
     *
     * @param logMessage
     * @param crudType
     * @param taskID
     * @param policyID
     * @param profileID
     */
    public void saveLog(String logMessage, CrudType crudType, Long taskID, Long policyID, Long profileID){
        log = new OperationLog();
        log.setLogMessage(logMessage);
        log.setCrudType(crudType);
        log.setTaskID(taskID);
        log.setPolicyID(policyID);
        log.setProfileID(profileID);
        logDao.create(log);
    }

    /**
     *
     * @param logMessage
     * @param crudType
     * @param taskID
     * @param policyID
     * @param profileID
     * @param requestData
     */
    public void saveLog(String logMessage, CrudType crudType, Long taskID, Long policyID, Long profileID, byte[] requestData){
        log = new OperationLog();
        log.setLogMessage(logMessage);
        log.setCrudType(crudType);
        log.setTaskID(taskID);
        log.setPolicyID(policyID);
        log.setProfileID(profileID);
        log.setRequestData(requestData);
        logDao.create(log);
    }

    /**
     *
     * @param logMessage
     * @param crudType
     * @param taskID
     * @param policyID
     * @param profileID
     * @param requestData
     * @param requestIp
     */
    public void saveLog(String logMessage, CrudType crudType, Long taskID, Long policyID, Long profileID, byte[] requestData, String requestIp){
        log = new OperationLog();
        log.setLogMessage(logMessage);
        log.setCrudType(crudType);
        log.setTaskID(taskID);
        log.setPolicyID(policyID);
        log.setProfileID(profileID);
        log.setRequestData(requestData);
        log.setRequestIP(requestIp);
        logDao.create(log);
    }
}
