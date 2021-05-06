package com.leverx.repository;

import com.leverx.model.GameObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository layer
 *
 * @author Andrew Panas
 */

@Repository
public interface GameObjectRepository extends JpaRepository<GameObject, Long> {
    @Query("SELECT go FROM GameObject go WHERE go.user.id = :id AND go.user.active = true")
    List<GameObject> findAllByTraderId(@Param("id") Long id);
}
