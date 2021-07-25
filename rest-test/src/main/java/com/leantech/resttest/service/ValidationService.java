package com.leantech.resttest.service;

import com.leantech.resttest.entity.Employee;
import com.leantech.resttest.entity.Person;
import com.leantech.resttest.entity.Position;
import com.leantech.resttest.repository.IEmployeeRepository;
import com.leantech.resttest.repository.IPersonRepository;
import com.leantech.resttest.repository.IPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {
    @Autowired
    IPositionRepository positionRepository;

    @Autowired
    IPersonRepository personRepository;

    @Autowired
    private IEmployeeRepository employeeRepository;

    /**
     * Validates if the fields on position object are correct
     * @param position
     * @param validateIdentifier: used only for update or delete operations
     * @return
     */
    public String validatePositionRequestFields(Position position, boolean validateIdentifier){
        String result = "Correct";
        if(position == null)
            result = "null position parameter";
        else {
            if(validateIdentifier && position.getId() == null)
                result = "null identifier";
            else {
                if (position.getName() == null || position.getName().isEmpty())
                    result = "null or invalid position name";
                else {
                    if (positionRepository.findByName(position.getName()) != null)
                        result = "position already exists on database";
                }
            }
        }

        return result;
    }

    /**
     * Validates if the received person object is correct to be saved in database
     * @param person
     * @param validateIdentifier: used only for update or delete operations
     * @return
     */
    public String validatePersonRequestFields(Person person, boolean validateIdentifier){
        String result = "Correct";
        if(person == null)
            result = "null person parameter";
        else {
            if(validateIdentifier && person.getId() == null)
                result = "null identifier";
            else {
                if (person.getName() == null || person.getName().isEmpty())
                    result = "null or invalid person name";
                else {
                    if (person.getId() != null && personRepository.findById(person.getId()) != null)
                        result = "person already exists on database";
                }
            }
        }


        return result;
    }

    /**
     * Used in the creation of an Employee record to verify that the received employee
     * object is correct including the existence of the Position and Person in database
     * @param employee
     * @return
     */
    public String validateEmployeeRequestFieldsForExistingCreation(Employee employee){
        String result = "Correct";
        if(employee == null)
            result = "null employee parameter";
        else {
            if(employee.getPerson() == null)
                result = "null person object";
            else {
                if (employee.getPerson().getId() == null || employee.getPerson().getId() <= 0)
                    result = "null or invalid person id";
                else {
                    if(employee.getPosition() == null || employee.getPosition().getId() <= 0){
                        result = "null, invalid or empty position";
                    } else {
                        if(employee.getSalary() == null || employee.getSalary() <= 0)
                            result = "null or invalid salary";
                    }
                }
            }
        }

        return result;
    }

    /**
     * Used in other operations like update or delete to verify that the employee object
     * is correct
     * @param employee
     * @return
     */
    public String validateEmployeeRequestFieldsForOtherOps(Employee employee){
        String result = "Correct";
        if(employee == null)
            result = "null employee parameter";
        else {
            if(employee.getId() == null)
                result = "null identifier";
            else {
                if(!employeeRepository.findById(employee.getId()).isPresent())
                    result = "employee not found";
            }
        }

        return result;
    }
}
