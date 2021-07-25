package com.leantech.resttest.controller;

import com.leantech.resttest.entity.Person;
import com.leantech.resttest.model.ErrorResponse;
import com.leantech.resttest.model.IdRequest;
import com.leantech.resttest.model.SuccessfulResponse;
import com.leantech.resttest.repository.IPersonRepository;
import com.leantech.resttest.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class PersonController {
    @Autowired
    IPersonRepository personRepository;

    @Autowired
    ValidationService validationService;

    /**
     * Creates a Person record on database if the person object received is valid
     * @param person: an object containing the fields to be created (id is not necessary)
     * @return
     */
    @PostMapping("/createPerson")
    public ResponseEntity<Object> createPerson(@RequestBody Person person){
        try {
            String validationResult = validationService.validatePersonRequestFields(person, false);
            if(validationResult.equals("Correct")) {
                return new ResponseEntity<>(personRepository.save(person), HttpStatus.CREATED);
            } else
                throw new IllegalArgumentException(validationResult);
        } catch (Exception exception){
            return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Modifies an existing Person record according to the person object received
     * @param person
     * @return
     */
    @PutMapping("/modifyPerson")
    public ResponseEntity<Object> modifyPerson(@RequestBody Person person){
        try {
            String validationResult = validationService.validatePersonRequestFields(person, true);
            if(validationResult.equals("person already exists on database")) {
                Optional<Person> personOnDatabase = personRepository.findById(person.getId());
                if(personOnDatabase.isPresent()) {
                    // set the new values for the columns
                    Person personToSave = personOnDatabase.get();
                    personToSave.setName(person.getName());
                    personToSave.setLastName(person.getLastName());
                    personToSave.setAddress(person.getAddress());
                    personToSave.setCityName(person.getCityName());
                    personToSave.setCellphone(person.getCellphone());

                    // save the modified object in the database
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

    /**
     * Deletes a Person record on database if the person is found
     * @param person
     * @return
     */
    @DeleteMapping("/deletePerson")
    public ResponseEntity<Object> deletePerson(@RequestBody Person person){
        try {
            String validationResult = validationService.validatePersonRequestFields(person, true);
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

    /**
     * Returns a Person record if the id is found in database
     * @param idRequest
     * @return
     */
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

    /**
     * Finds all records of Person in database
     * @return
     */
    @GetMapping("/findAllPeople")
    public ResponseEntity<Object> findAllPeople(){
        try{
            return new ResponseEntity<>(personRepository.findAll(), HttpStatus.OK);
        } catch (Exception exception){
            return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
        }
    }


}
