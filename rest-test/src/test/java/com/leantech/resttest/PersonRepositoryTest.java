package com.leantech.resttest;

import com.leantech.resttest.entity.Person;
import com.leantech.resttest.repository.IPersonRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class PersonRepositoryTest {
    @Autowired
    private IPersonRepository personRepository;

    @Test
    public void whenCreatingPerson_ReturnFullObject(){
        Person person = new Person("Sacarias", "Plata", "avenida siempre viva", "3219877654", "Bogotá");
        personRepository.save(person);

        List<Person> foundPeople = personRepository.findAll();

        Assertions.assertThat(foundPeople.size()).isEqualTo(3);
        Assertions.assertThat(foundPeople.get(2).getName()).isEqualTo("Sacarias");
        Assertions.assertThat(foundPeople.get(2).getCellphone()).isEqualTo("3219877654");
        Assertions.assertThat(foundPeople.get(2).getId()).isEqualTo(3);
    }

    @Test
    public void whenModifyingPerson_ReturnCorrectCityName(){
        System.out.println("Records on Person table: " + personRepository.findAll().size());
        Person person = personRepository.findById(2L).get();
        Assertions.assertThat(person.getName()).isEqualTo("Jessica");

        person.setCityName("Cali");
        Assertions.assertThat(personRepository.save(person).getCityName()).isEqualTo("Cali");
    }
}
