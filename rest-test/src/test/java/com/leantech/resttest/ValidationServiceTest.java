package com.leantech.resttest;

import com.leantech.resttest.entity.Employee;
import com.leantech.resttest.entity.Person;
import com.leantech.resttest.entity.Position;
import com.leantech.resttest.repository.IEmployeeRepository;
import com.leantech.resttest.repository.IPersonRepository;
import com.leantech.resttest.repository.IPositionRepository;
import com.leantech.resttest.service.ValidationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidationServiceTest {
    @Autowired
    private IEmployeeRepository employeeRepository;

    @Autowired
    IPersonRepository personRepository;

    @Autowired
    IPositionRepository positionRepository;

    ValidationService validationService = new ValidationService();

    @Test
    public void whenTestingPosition(){
        Position position = new Position();
        position.setName("");

        Assertions.assertThat(validationService.validatePositionRequestFields(position, false))
                .isEqualTo("null or invalid position name");

        Assertions.assertThat(validationService.validatePositionRequestFields(position, true))
                .isEqualTo("null identifier");
    }

    @Test
    public void whenTestingPerson(){
        Person person = new Person();
        person.setName("");

        Assertions.assertThat(validationService.validatePersonRequestFields(person, false))
                .isEqualTo("null or invalid person name");

        Assertions.assertThat(validationService.validatePersonRequestFields(person, true))
                .isEqualTo("null identifier");
    }

    @Test
    public void whenTestingEmployee(){
        Employee employee = new Employee();
        employee.setSalary(null);

        Person person = new Person();
        person.setId(-1L);
        person.setName("Raul");

        Assertions.assertThat(validationService.validateEmployeeRequestFieldsForExistingCreation(employee))
                .isEqualTo("null person object");

        employee.setPerson(person);
        Assertions.assertThat(validationService.validateEmployeeRequestFieldsForExistingCreation(employee))
                .isEqualTo("null or invalid person id");

        person.setId(4L);
        Position position = new Position();
        position.setId(-1L);
        Assertions.assertThat(validationService.validateEmployeeRequestFieldsForExistingCreation(employee))
                .isEqualTo("null, invalid or empty position");

        position.setId(3L);
        employee.setSalary(-20L);
        employee.setPosition(position);
        Assertions.assertThat(validationService.validateEmployeeRequestFieldsForExistingCreation(employee))
                .isEqualTo("null or invalid salary");

    }
}
