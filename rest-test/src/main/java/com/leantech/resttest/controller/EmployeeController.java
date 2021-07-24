package com.leantech.resttest.controller;

import com.leantech.resttest.entity.Employee;
import com.leantech.resttest.entity.Position;
import com.leantech.resttest.model.ErrorResponse;
import com.leantech.resttest.model.IdRequest;
import com.leantech.resttest.model.PositionEmployeesResponse;
import com.leantech.resttest.model.SuccessfulResponse;
import com.leantech.resttest.repository.IEmployeeRepository;
import com.leantech.resttest.repository.IPersonRepository;
import com.leantech.resttest.repository.IPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class EmployeeController {
    @Autowired
    private IEmployeeRepository employeeRepository;
    @Autowired
    IPersonRepository personRepository;

    @Autowired
    IPositionRepository positionRepository;

    /**
     * Creates an Employee including a Position and Person records on database
     * @param employee
     * @return
     */
    @PostMapping("/createEmployeeFromScratch")
    public ResponseEntity<Object> createEmployeeFromScratch(@RequestBody Employee employee){
        try {
            String validationResult = validateRequestFieldsForCreation(employee);
            if(validationResult.equals("Correct")) {
                return new ResponseEntity<>(employeeRepository.save(employee), HttpStatus.CREATED);
            } else
                throw new IllegalArgumentException(validationResult);
        } catch (Exception exception){
            return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Creates an Employee record verifying firstly if the Position and Person objects exists in the database
     * @param employee
     * @return
     */
    @PostMapping("/createEmployeeFromExisting")
    public ResponseEntity<Object> createEmployeeFromExisting(@RequestBody Employee employee){
        try {
            String validationResult = validateRequestFieldsForExistingCreation(employee);
            if(validationResult.equals("Correct")) {
                employee.setPerson(personRepository.findById(employee.getPerson().getId()).get());
                employee.setPosition(positionRepository.findById(employee.getPosition().getId()).get());
                return new ResponseEntity<>(employeeRepository.save(employee), HttpStatus.CREATED);
            } else
                throw new IllegalArgumentException(validationResult);
        } catch (Exception exception){
            return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Modifies an existing Employee record in database. Only the salary and position can be modified
     * @param employee
     * @return
     */
    @PutMapping("/modifyEmployee")
    public ResponseEntity<Object> modifyEmployee(@RequestBody Employee employee){
        try {
            String validationResult = validateRequestFieldsForOtherOps(employee);
            if(validationResult.equals("Correct")) {
                Optional<Employee> employeeOnDatabase = employeeRepository.findById(employee.getId());
                if(employeeOnDatabase.isPresent()) {
                    Employee employeeToSave = employeeOnDatabase.get();
                    employeeToSave.setSalary(employee.getSalary());
                    if(employee.getPosition() != null){
                        if(employee.getPosition().getId() != null && positionRepository.findById(employee.getPosition().getId()).isPresent()){
                            employeeToSave.setPosition(positionRepository.findById(employee.getPosition().getId()).get());
                        }
                    }
                    employeeRepository.save(employeeToSave);
                    return new ResponseEntity<>(new SuccessfulResponse("Employee record successfully modified"), HttpStatus.OK);
                } else
                    return new ResponseEntity<>(new ErrorResponse("Employee not found in database"), HttpStatus.NOT_FOUND);
            } else
                throw new IllegalArgumentException(validationResult);
        } catch (Exception exception){
            return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes an existing Employee record in database
     * @param employee
     * @return
     */
    @DeleteMapping("/deleteEmployee")
    public ResponseEntity<Object> deleteEmployee(@RequestBody Employee employee){
        try {
            String validationResult = validateRequestFieldsForOtherOps(employee);
            if(validationResult.equals("Correct")) {
                Optional<Employee> employeeOnDatabase = employeeRepository.findById(employee.getId());
                if(employeeOnDatabase.isPresent()) {
                    Employee employeeToDelete = employeeOnDatabase.get();
                    employeeRepository.delete(employeeToDelete);
                    return new ResponseEntity<>(new SuccessfulResponse("Employee record successfully deleted"), HttpStatus.OK);
                } else
                    return new ResponseEntity<>(new ErrorResponse("Employee not found in database"), HttpStatus.NOT_FOUND);
            } else
                throw new IllegalArgumentException(validationResult);
        } catch (Exception exception){
            return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Finds an Employee record by its ID
     * @param idRequest
     * @return
     */
    @GetMapping("/findEmployeeById")
    public ResponseEntity<Object> findEmployeeById(@RequestBody IdRequest idRequest){
        try {
            if(idRequest != null && idRequest.getId() > 0){
                Optional<Employee> employeeOnDatabase = employeeRepository.findById(idRequest.getId());
                if(employeeOnDatabase.isPresent()) {
                    Employee foundEmployee = employeeOnDatabase.get();
                    return new ResponseEntity<>(foundEmployee, HttpStatus.OK);
                } else
                    return new ResponseEntity<>(new ErrorResponse("Employee not found in database"), HttpStatus.NOT_FOUND);
            } else
                throw new IllegalArgumentException("Null or invalid ID");
        } catch (Exception exception){
            return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Returns al the existing Employee record in database
     * @param positionName: If present will return only the Employees associated with the position
     * @param employeeName: If present will return only the Employees containing the employeeName in
     *                    associated Person record
     * @return
     */
    @GetMapping("/findAllEmployee")
    public ResponseEntity<Object> findAllEmployee(@RequestParam("positionName") Optional<String> positionName,
                                                  @RequestParam("employeeName") Optional<String> employeeName){
        try{
            if(!positionName.isPresent() && !employeeName.isPresent())
                return new ResponseEntity<>(employeeRepository.findAll(), HttpStatus.OK);
            else {
                List<Employee> resultList = new ArrayList<>();
                if (positionName.isPresent() && !positionName.get().isEmpty()) {
                    List<Employee> searchList = employeeRepository.findEmployeeByPosition_Name(positionName.get());
                    if (searchList != null && !searchList.isEmpty()) {
                        if (employeeName.isPresent()) {
                            for(Employee employee : searchList){
                                if(employee.getPerson().getName().contains(employeeName.get()))
                                    resultList.add(employee);
                            }
                        } else
                            resultList.addAll(searchList);
                    }
                } else {
                    if (employeeName.isPresent() && !employeeName.get().isEmpty()) {
                        List<Employee> searchList = employeeRepository.findEmployeeByPerson_Name(employeeName.get());
                        if (searchList != null && !searchList.isEmpty())
                            resultList.addAll(searchList);
                    }
                }
                return new ResponseEntity<>(resultList, HttpStatus.OK);
            }
        } catch (Exception exception){
            return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Returns a custom response containing every Position with all the Employees associated with
     * that Position
     * @return
     */
    @GetMapping("/findAllEmployeesByPosition")
    public ResponseEntity<Object> findAllEmployeesByPosition(){
        try {
            List<PositionEmployeesResponse> positionEmployeesResponses = new ArrayList<>();
            List<Position> allPositions = positionRepository.findAll();
            for(Position position : allPositions){
                PositionEmployeesResponse positionEmployeesResponse = new PositionEmployeesResponse();
                positionEmployeesResponse.setId(position.getId());
                positionEmployeesResponse.setName(position.getName());
                List<Employee> positionEmployees = employeeRepository.findEmployeeByPosition_Name(position.getName());
                positionEmployeesResponse.setEmployees(positionEmployees);
                positionEmployeesResponses.add(positionEmployeesResponse);
            }
            return new ResponseEntity<>(positionEmployeesResponses, HttpStatus.OK);
        } catch (Exception exception){
            return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Used in the creation of an Employee record to verify that the employee object is correct
     * @param employee
     * @return
     */
    private String validateRequestFieldsForCreation(Employee employee){
        String result = "Correct";
        if(employee == null)
            result = "null employee parameter";
        else {
            if(employee.getPerson() == null)
                result = "null person object";
            else {
                if (employee.getPerson().getName() == null || employee.getPerson().getName().isEmpty())
                    result = "null or invalid person name";
                else {
                    if(employee.getPosition() == null || employee.getPosition().getName().isEmpty()){
                        result = "null or empty position";
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
     * Used in the creation of an Employee record to verify that the received employee
     * object is correct including the existence of the Position and Person in database
     * @param employee
     * @return
     */
    private String validateRequestFieldsForExistingCreation(Employee employee){
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
    private String validateRequestFieldsForOtherOps(Employee employee){
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
