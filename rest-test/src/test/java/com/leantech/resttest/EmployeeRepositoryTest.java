package com.leantech.resttest;

import com.leantech.resttest.entity.Employee;
import com.leantech.resttest.entity.Person;
import com.leantech.resttest.entity.Position;
import com.leantech.resttest.repository.IEmployeeRepository;
import com.leantech.resttest.repository.IPersonRepository;
import com.leantech.resttest.repository.IPositionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class EmployeeRepositoryTest {
    @Autowired
    private IEmployeeRepository employeeRepository;

    @Autowired
    private IPositionRepository positionRepository;

    @Autowired
    private IPersonRepository personRepository;

    @Test
    public void whenCreatingEmployee_ReturnFullObject(){
        Position position = positionRepository.findAll().get(1);
        Person person = personRepository.findAll().get(1);
        Employee employee = new Employee(person, position, 400L);
        employeeRepository.save(employee);

        List<Employee> foundEmployees = employeeRepository.findAll();

        Assertions.assertThat(foundEmployees.size()).isEqualTo(3);
        Assertions.assertThat(foundEmployees.get(2).getPosition().getName()).isEqualTo("Architect");
        Assertions.assertThat(foundEmployees.get(2).getPerson().getName()).isEqualTo("Jessica");
        Assertions.assertThat(foundEmployees.get(2).getSalary()).isEqualTo(400L);
        Assertions.assertThat(foundEmployees.get(2).getId()).isEqualTo(3);
    }

    @Test
    public void whenModifyingEmployee_ReturnCorrectSalary(){
        Position position = positionRepository.findByName("Architect");
        Employee employee = employeeRepository.findEmployeeByPerson_Name("Armando").get(0);
        employee.setPosition(position);

        Assertions.assertThat(employeeRepository.save(employee).getPosition().getName()).isEqualTo("Architect");
    }
}
