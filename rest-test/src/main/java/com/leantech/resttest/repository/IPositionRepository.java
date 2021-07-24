package com.leantech.resttest.repository;

import com.leantech.resttest.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPositionRepository extends JpaRepository<Position, Long> {
    Position findByName(String name);
}
