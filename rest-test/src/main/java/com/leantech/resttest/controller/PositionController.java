package com.leantech.resttest.controller;

import com.leantech.resttest.entity.Position;
import com.leantech.resttest.model.ErrorResponse;
import com.leantech.resttest.model.IdRequest;
import com.leantech.resttest.model.PositionRequest;
import com.leantech.resttest.model.SuccessfulResponse;
import com.leantech.resttest.repository.IPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class PositionController {
    @Autowired
    IPositionRepository positionRepository;

    @PostMapping("/createPosition")
    public ResponseEntity<Object> createPosition(@RequestBody PositionRequest positionRequest){
        try {
            Position positionToVerify = new Position(positionRequest.getPositionName());
            String validationResult = validateRequestFields(positionToVerify, false);
            if(validationResult.equals("Correct")) {
                return new ResponseEntity<>(positionRepository.save(positionToVerify), HttpStatus.CREATED);
            } else
                throw new IllegalArgumentException(validationResult);
        } catch (Exception exception){
            return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/modifyPosition")
    public ResponseEntity<Object> modifyPosition(@RequestBody Position position){
        try {
            String validationResult = validateRequestFields(position, true);
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

    @DeleteMapping("/deletePosition")
    public ResponseEntity<Object> deletePosition(@RequestBody Position position){
        try {
            String validationResult = validateRequestFields(position, true);
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

    @GetMapping("/findAllPositions")
    public ResponseEntity<Object> findAllPositions(){
        try{
            return new ResponseEntity<>(positionRepository.findAll(), HttpStatus.OK);
        } catch (Exception exception){
            return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    private String validateRequestFields(Position position, boolean validateIdentifier){
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
}
