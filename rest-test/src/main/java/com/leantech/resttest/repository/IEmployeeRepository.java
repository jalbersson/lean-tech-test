package com.leantech.resttest.repository;

import com.leantech.resttest.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Long> {
    /**
     * Finds all the Employee records on database containing the Person name
     * @param name
     * @return
     */
    List<Employee> findEmployeeByPerson_Name(String name);

    /**
     * Finds all the Employee records on database associated with the Position
     * @param name
     * @return
     */
    List<Employee> findEmployeeByPosition_Name(String name);
}
