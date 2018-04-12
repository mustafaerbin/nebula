package com.tr.nebula.log.entity;

import com.tr.nebula.persistence.jpa.domain.BaseEntity;
import javax.persistence.*;
import java.util.Arrays;

/**
 * Created by Ali Kızılırmak on 16.05.2017.
 */
@Entity
public class OperationLog extends BaseEntity {

    @Column(name = "CRUD_TYPE")
    @Enumerated(EnumType.STRING)
    private CrudType crudType;

    @Column(name = "TASK_ID")
    private Long taskID;

    @Column(name = "POLICY_ID")
    private Long policyID;

    @Column(name = "PROFILE_ID")
    private Long profileID;

    @Column(name = "LOG_MESSAGE", nullable = false)
    private String logMessage;

    @Lob
    @Column(name = "REQUEST_DATA")
    private byte[] requestData;

    @Column(name = "REQUEST_IP")
    private String requestIP;

    public OperationLog() {
    }

    public OperationLog(CrudType crudType, Long taskID, Long policyID, Long profileID,
                        String logMessage, byte[] requestData, String requestIP) {
        this.crudType = crudType;
        this.taskID = taskID;
        this.policyID = policyID;
        this.profileID = profileID;
        this.logMessage = logMessage;
        this.requestData = requestData;
        this.requestIP = requestIP;
    }

    public CrudType getCrudType() {
        return crudType;
    }

    public void setCrudType(CrudType crudType) {
        this.crudType = crudType;
    }

    public Long getTaskID() {
        return taskID;
    }

    public void setTaskID(Long taskID) {
        this.taskID = taskID;
    }

    public Long getPolicyID() {
        return policyID;
    }

    public void setPolicyID(Long policyID) {
        this.policyID = policyID;
    }

    public Long getProfileID() {
        return profileID;
    }

    public void setProfileID(Long profileID) {
        this.profileID = profileID;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }

    public byte[] getRequestData() {
        return requestData;
    }

    public void setRequestData(byte[] requestData) {
        this.requestData = requestData;
    }

    public String getRequestIP() {
        return requestIP;
    }

    public void setRequestIP(String requestIP) {
        this.requestIP = requestIP;
    }

    @Override
    public String toString() {
        return "OperationLog{" +
                "crudType=" + crudType +
                ", taskID=" + taskID +
                ", policyID=" + policyID +
                ", profileID=" + profileID +
                ", logMessage='" + logMessage + '\'' +
                ", requestData=" + Arrays.toString(requestData) +
                ", requestIP='" + requestIP + '\'' +
                '}';
    }
}
