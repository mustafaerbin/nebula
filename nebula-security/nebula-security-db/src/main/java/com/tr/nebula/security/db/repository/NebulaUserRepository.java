package com.tr.nebula.security.db.repository;

import com.tr.nebula.security.db.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Mustafa Erbin on 21.03.2017.
 */
@Repository
public interface NebulaUserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByUsername(String username);
}
