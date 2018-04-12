package service;

import com.tr.nebula.log.dao.OperationLogDao;
import com.tr.nebula.log.entity.CrudType;
import com.tr.nebula.log.entity.OperationLog;
import com.tr.nebula.log.service.OperationLogService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by Mustafa Erbin on 25.05.2017.
 */
@RunWith(SpringRunner.class)
public class OperationLogTest {

    EntityManagerFactory factory;

    EntityManager entityManager;

    @MockBean
    public OperationLogService operationLogService;

    @MockBean
    public OperationLogDao operationLogDao;

    final String logMessage = "Hello JUnit";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        factory = Persistence.createEntityManagerFactory("test");
        entityManager = factory.createEntityManager();
        operationLogDao = new OperationLogDao(entityManager);
        operationLogService = new OperationLogService(operationLogDao);
    }

    @Test
    public void saveLogWithMessage() {
        operationLogService.saveLog(logMessage);
        OperationLog log = operationLogDao.findById(1L);
        assertThat(log).isNotNull();
        assertThat(log.getLogMessage()).isEqualTo(logMessage);

    }

    @Test
    public void saveLogWithCrudType(){
        operationLogService.saveLog(logMessage, CrudType.INSERT);
        OperationLog log = operationLogDao.findById(1L);
        assertThat(log).isNotNull();
        assertThat(log.getCrudType()).isEqualTo(CrudType.INSERT);
    }

    @Test
    public void saveLogWithTaskID(){
        operationLogService.saveLog(logMessage, CrudType.INSERT, 1L);
        OperationLog log = operationLogDao.findById(1L);
        assertThat(log).isNotNull();
        assertThat(log.getTaskID()).isEqualTo(1L);
    }

    @Test
    public void saveLogWithPolicyID(){
        operationLogService.saveLog(logMessage, CrudType.INSERT, null, 1L);
        OperationLog log = operationLogDao.findById(1L);
        assertThat(log).isNotNull();
        assertThat(log.getPolicyID()).isEqualTo(1L);
    }

    @Test
    public void saveLogWithProfileID(){
        operationLogService.saveLog(logMessage, CrudType.INSERT, null, null, 1L);
        OperationLog log = operationLogDao.findById(1L);
        assertThat(log).isNotNull();
        assertThat(log.getProfileID()).isEqualTo(1L);
    }

    @Test
    public void saveLogWithRequestData(){
        operationLogService.saveLog(logMessage, CrudType.INSERT, null, null, null, logMessage.getBytes());
        OperationLog log = operationLogDao.findById(1L);
        assertThat(log).isNotNull();
        assertThat(log.getRequestData()).isEqualTo(logMessage.getBytes());
    }

    @Test
    public void saveLogWithRequestIp(){
        operationLogService.saveLog(logMessage, CrudType.INSERT, null, null, null, null, "192.168.1.1");
        OperationLog log = operationLogDao.findById(1L);
        assertThat(log).isNotNull();
        assertThat(log.getRequestIP()).isEqualTo("192.168.1.1");
    }
}
