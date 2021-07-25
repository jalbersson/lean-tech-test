package com.leantech.resttest.controller;

import com.leantech.resttest.entity.Position;
import com.leantech.resttest.model.ErrorResponse;
import com.leantech.resttest.model.IdRequest;
import com.leantech.resttest.model.PositionRequest;
import com.leantech.resttest.model.SuccessfulResponse;
import com.leantech.resttest.repository.IPositionRepository;
import com.leantech.resttest.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class PositionController {
    @Autowired
    IPositionRepository positionRepository;

    @Autowired
    ValidationService validationService;

    /**
     * Creates a Position record in the database. Only the name of the position is needed and validated
     * @param positionRequest
     * @return
     */
    @PostMapping("/createPosition")
    public ResponseEntity<Object> createPosition(@RequestBody PositionRequest positionRequest){
        try {
            Position positionToVerify = new Position(positionRequest.getPositionName());
            String validationResult = validationService.validatePositionRequestFields(positionToVerify, false);
            if(validationResult.equals("Correct")) {
                return new ResponseEntity<>(positionRepository.save(positionToVerify), HttpStatus.CREATED);
            } else
                throw new IllegalArgumentException(validationResult);
        } catch (Exception exception){
            return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Modifies an existing Position record in database
     * @param position
     * @return
     */
    @PutMapping("/modifyPosition")
    public ResponseEntity<Object> modifyPosition(@RequestBody Position position){
        try {
            String validationResult = validationService.validatePositionRequestFields(position, true);
            if(position != null && position.getId() != null) {
                Optional<Position> positionOnDatabase = positionRepository.findById(position.getId());
                if(positionOnDatabase.isPresent()) {
                    Position positionToSave = positionOnDatabase.get();
                    positionToSave.setName(position.getName());
                    positionRepository.save(positionToSave);
                    return new ResponseEntity<>(new SuccessfulResponse("Position successfully modified"), HttpStatus.OK);
                } else
                    return new ResponseEntity<>(new ErrorResponse("position not found in database"), HttpStatus.NOT_FOUND);
            } else
                throw new IllegalArgumentException(validationResult);
        } catch (Exception exception){
            return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes an existing Position record in database if Id is found
     * @param position
     * @return
     */
    @DeleteMapping("/deletePosition")
    public ResponseEntity<Object> deletePosition(@RequestBody Position position){
        try {
            String validationResult = validationService.validatePositionRequestFields(position, true);
            if(position != null && position.getId() != null) {
                Optional<Position> positionOnDatabase = positionRepository.findById(position.getId());
                if(positionOnDatabase.isPresent()) {
                    Position positionToDelete = positionOnDatabase.get();
                    positionRepository.delete(positionToDelete);
                    return new ResponseEntity<>(new SuccessfulResponse("Position successfully deleted"), HttpStatus.OK);
                } else
                    return new ResponseEntity<>(new ErrorResponse("position not found in database"), HttpStatus.NOT_FOUND);
            } else
                throw new IllegalArgumentException(validationResult);
        } catch (Exception exception){
            return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Finds a Position record by its ID
     * @param idRequest
     * @return
     */
    @GetMapping("/findPositionById")
    public ResponseEntity<Object> findPositionById(@RequestBody IdRequest idRequest){
        try {
            if(idRequest != null && idRequest.getId() > 0){
                Optional<Position> positionOnDatabase = positionRepository.findById(idRequest.getId());
                if(positionOnDatabase.isPresent()) {
                    Position foundPosition = positionOnDatabase.get();
                    return new ResponseEntity<>(foundPosition, HttpStatus.OK);
                } else
                    return new ResponseEntity<>(new ErrorResponse("position not found in database"), HttpStatus.NOT_FOUND);
            } else
                throw new IllegalArgumentException("Null or invalid ID");
        } catch (Exception exception){
            return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Returns all existing Position records
     * @return
     */
    @GetMapping("/findAllPositions")
    public ResponseEntity<Object> findAllPositions(){
        try{
            return new ResponseEntity<>(positionRepository.findAll(), HttpStatus.OK);
        } catch (Exception exception){
            return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
