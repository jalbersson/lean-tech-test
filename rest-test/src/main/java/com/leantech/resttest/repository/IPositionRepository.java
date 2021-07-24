package com.leantech.resttest.repository;

import com.leantech.resttest.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPositionRepository extends JpaRepository<Position, Long> {
    /**
     * Finds a Position by its name (there is a restriction to prevent the creation of
     * a Position with the same name of an existing one)
     * @param name
     * @return
     */
    Position findByName(String name);
}
