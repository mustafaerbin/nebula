package com.tr.nebula.security.db.repository;

import com.tr.nebula.security.db.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Mustafa Erbin on 29.03.2017.
 */
@Repository
public interface NebulaMenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByParent_idOrderByIndexAsc(Long parentOid);
}
