package com.leantech.resttest.controller;

import com.leantech.resttest.entity.Person;
import com.leantech.resttest.model.ErrorResponse;
import com.leantech.resttest.model.IdRequest;
import com.leantech.resttest.model.SuccessfulResponse;
import com.leantech.resttest.repository.IPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class PersonController {
    @Autowired
    IPersonRepository personRepository;

    @PostMapping("/createPerson")
    public ResponseEntity<Object> createPerson(@RequestBody Person person){
        try {
            String validationResult = validateRequestFields(person, false);
            if(validationResult.equals("Correct")) {
                return new ResponseEntity<>(personRepository.save(person), HttpStatus.CREATED);
            } else
                throw new IllegalArgumentException(validationResult);
        } catch (Exception exception){
            return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/modifyPerson")
    public ResponseEntity<Object> modifyPerson(@RequestBody Person person){
        try {
            String validationResult = validateRequestFields(person, true);
            if(validationResult.equals("person already exists on database")) {
                Optional<Person> personOnDatabase = personRepository.findById(person.getId());
                if(personOnDatabase.isPresent()) {
                    Person personToSave = personOnDatabase.get();
                    personToSave.setName(person.getName());
                    personToSave.setLastName(person.getLastName());
                    personToSave.setAddress(person.getAddress());
                    personToSave.setCityName(person.getCityName());
                    personToSave.setCellphone(person.getCellphone());
                    personRepository.save(personToSave);
                    return new ResponseEntity<>(new SuccessfulResponse("Person record successfully modified"), HttpStatus.OK);
                } else
                    return new ResponseEntity<>(new ErrorResponse("Person not found in database"), HttpStatus.NOT_FOUND);
            } else
                throw new IllegalArgumentException(validationResult);
        } catch (Exception exception){
            return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deletePerson")
    public ResponseEntity<Object> deletePerson(@RequestBody Person person){
        try {
            String validationResult = validateRequestFields(person, true);
            if(validationResult.equals("person already exists on database")) {
                Optional<Person> personOnDatabase = personRepository.findById(person.getId());
                if(personOnDatabase.isPresent()) {
                    Person personToDelete = personOnDatabase.get();
                    personRepository.delete(personToDelete);
                    return new ResponseEntity<>(new SuccessfulResponse("Person record successfully deleted"), HttpStatus.OK);
                } else
                    return new ResponseEntity<>(new ErrorResponse("Person not found in database"), HttpStatus.NOT_FOUND);
            } else
                throw new IllegalArgumentException(validationResult);
        } catch (Exception exception){
            return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findPersonById")
    public ResponseEntity<Object> findPersonById(@RequestBody IdRequest idRequest){
        try {
            if(idRequest != null && idRequest.getId() > 0){
                Optional<Person> personOnDatabase = personRepository.findById(idRequest.getId());
                if(personOnDatabase.isPresent()) {
                    Person foundPerson = personOnDatabase.get();
                    return new ResponseEntity<>(foundPerson, HttpStatus.OK);
                } else
                    return new ResponseEntity<>(new ErrorResponse("Person not found in database"), HttpStatus.NOT_FOUND);
            } else
                throw new IllegalArgumentException("Null or invalid ID");
        } catch (Exception exception){
            return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findAllPeople")
    public ResponseEntity<Object> findAllPeople(){
        try{
            return new ResponseEntity<>(personRepository.findAll(), HttpStatus.OK);
        } catch (Exception exception){
            return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    private String validateRequestFields(Person person, boolean validateIdentifier){
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
}
