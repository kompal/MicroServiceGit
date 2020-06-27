package com.telstra.codechallenge.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@RestController
public class GitUserController {
    private static final Logger LOGGER = LogManager.getLogger(GitUserController.class.getName());

    private GitUserService gitUserService;
    private RestTemplate restTemplate;

    public GitUserController(
            GitUserService gitUserService) {
        this.gitUserService = gitUserService;
    }

    public void setGitUserService(GitUserService gitUserService){
        this.gitUserService = new GitUserService(restTemplate);
    }

    @RequestMapping(path ="/user/{number}", method = RequestMethod.GET)
    public ResponseEntity getUsers(@PathVariable("number") @Valid Integer number) throws Exception {

        if(number>100){
            LOGGER.debug("More than 100 users requested");
            ResponseEntity<String> responseEntityLimitExceeded= new ResponseEntity("Page can display 100 records at a time, you've exceeded the max user retrieval limit",HttpStatus.BAD_REQUEST);
            return responseEntityLimitExceeded;
        }else{
            UserModel userModel=gitUserService.getUsers(number);
            ResponseEntity<UserModel> responseEntity = new ResponseEntity(userModel,HttpStatus.ACCEPTED);
            return responseEntity;
        }
    }
}
