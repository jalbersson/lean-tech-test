package com.leantech.resttest.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Person person;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Position position;

    @Column(name = "salary")
    private Long salary;

    public Employee() {
    }

    public Employee(Person person, Position position, Long salary) {
        this.person = person;
        this.position = position;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id.equals(employee.id) && person.equals(employee.person) && position.equals(employee.position) && salary.equals(employee.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person, position, salary);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", person=" + person +
                ", position=" + position +
                ", salary=" + salary +
                '}';
    }
}
