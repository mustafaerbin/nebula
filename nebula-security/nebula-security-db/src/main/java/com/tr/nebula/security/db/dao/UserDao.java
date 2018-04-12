package com.tr.nebula.security.db.dao;

import com.tr.nebula.persistence.jpa.dao.BaseDaoImpl;
import com.tr.nebula.security.db.domain.User;
import org.springframework.stereotype.Service;

/**
 * Created by Mustafa Erbin on 17.04.2017.
 */
@Service
public class UserDao extends BaseDaoImpl<User, Long> {
}
