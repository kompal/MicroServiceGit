package com.telstra.codechallenge.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class GitUserController {
    private static final Logger LOGGER = LogManager.getLogger(GitUserController.class.getName());

    @Autowired
    private GitUserService gitUserService;

    public GitUserController(
            GitUserService gitUserService) {
        this.gitUserService = gitUserService;
    }

    @RequestMapping(path ="/user/{number}", method = RequestMethod.GET)
    public ResponseEntity getUsers(@PathVariable("number") @Valid Integer number) throws Exception {

        if(number>100){
            LOGGER.debug("More than 100 users requested");
            ResponseEntity<String> responseEntityLimitExceeded= new ResponseEntity("Page can display 100 records at a time, you've exceeded the max user retrieval limit",HttpStatus.BAD_REQUEST);
            return responseEntityLimitExceeded;
        }else if(number<=0){
            LOGGER.debug("Please provide valid user number >0");
            ResponseEntity<String> responseEntityLessThanZero= new ResponseEntity("Please provide valid user number >0",HttpStatus.NOT_ACCEPTABLE);
            return responseEntityLessThanZero;
        }
        else{
            UserModel userModel=gitUserService.getUsers(number);
            ResponseEntity<UserModel> responseEntity;
            if(userModel!=null){
                if(userModel.getItems()==null){
                    LOGGER.debug("Git Server returned null");
                    responseEntity= new ResponseEntity("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
                }else{
                    responseEntity = new ResponseEntity(userModel,HttpStatus.ACCEPTED);
                }
            }
            else{
                responseEntity = new ResponseEntity("API returned empty List of users",HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return responseEntity;
        }
    }
}
