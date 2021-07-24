package com.leantech.resttest.model;

import com.leantech.resttest.entity.Employee;
import java.io.Serializable;
import java.util.List;

/**
 * This class is used to create a custom response asked in one of the points of the exercise
 */
public class PositionEmployeesResponse implements Serializable {
    private Long id;
    private String name;
    private List<Employee> employees;

    public PositionEmployeesResponse() {
    }

    public PositionEmployeesResponse(Long id, String name, List<Employee> employees) {
        this.id = id;
        this.name = name;
        this.employees = employees;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
