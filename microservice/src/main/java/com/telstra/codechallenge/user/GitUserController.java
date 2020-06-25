package com.telstra.codechallenge.user;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class GitUserController {
    private GitUserService gitUserService;


    public GitUserController(
            GitUserService gitUserService) {
        this.gitUserService = gitUserService;
    }

    @RequestMapping(path ="/user/{number}", method = RequestMethod.GET)
    public UserModel getWebsites(@PathVariable("number") @Valid Integer number) throws Exception {

        return gitUserService.getUsers(number);
    }

}
