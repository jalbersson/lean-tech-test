package com.leantech.resttest.repository;

import com.leantech.resttest.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findEmployeeByPerson_Name(String name);
    List<Employee> findEmployeeByPosition_Name(String name);
}
