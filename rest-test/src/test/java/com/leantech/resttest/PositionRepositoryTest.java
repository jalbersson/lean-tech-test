package com.leantech.resttest;

import com.leantech.resttest.entity.Position;
import com.leantech.resttest.repository.IPositionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class PositionRepositoryTest {
    @Autowired
    private IPositionRepository positionRepository;

    @Test
    public void whenCreatingPosition_ReturnFullObject(){
        Position position = new Position("Data Scientist");
        positionRepository.save(position);

        List<Position> foundPositions = positionRepository.findAll();

        Assertions.assertThat(foundPositions.size()).isEqualTo(3);
        Assertions.assertThat(foundPositions.get(2).getName()).isEqualTo("Data Scientist");
        Assertions.assertThat(foundPositions.get(2).getId()).isEqualTo(3);
    }

    @Test
    public void whenModifyingPosition_ReturnCorrectName(){
        Position position = positionRepository.findByName("Architect");
        Assertions.assertThat(position.getName()).isEqualTo("Architect");

        position.setName("Data Scientist");
        Assertions.assertThat(positionRepository.save(position).getName()).isEqualTo("Data Scientist");
    }
}
