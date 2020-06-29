package com.telstra.codechallenge.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

public class MockGitUserController {

    @RequestMapping(path ="/user/{number}", method = RequestMethod.GET)
    public ResponseEntity getUsers(@PathVariable("number") @Valid Integer number) throws Exception {

            ResponseEntity<String> responseEntity= new ResponseEntity("Test Response", HttpStatus.INTERNAL_SERVER_ERROR);
            return responseEntity;

    }

    @RequestMapping(path ="/user/{number}", method = RequestMethod.GET)
    public ResponseEntity getUsersInavlidRequest(@PathVariable("number") @Valid Integer number) throws Exception {

        ResponseEntity<String> responseEntity= new ResponseEntity("Please provide valid user number >0", HttpStatus.NOT_ACCEPTABLE);
        return responseEntity;

    }
}
