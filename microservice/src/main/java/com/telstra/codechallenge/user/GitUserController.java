package com.telstra.codechallenge.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class GitUserController {
    private GitUserService gitUserService;
    @Autowired
    private Environment env;

    public GitUserController(
            GitUserService gitUserService) {
        this.gitUserService = gitUserService;
    }

    @RequestMapping(path ="/user/{number}", method = RequestMethod.GET)
    public List<UserModel.Item> getWebsites(@PathVariable("number") @Valid Integer number) throws Exception {

        return gitUserService.getWebsites(number);
    }

    @RequestMapping(path ="/user2/{number}", method = RequestMethod.GET)
    public ResponseEntity getWebsites2(@PathVariable("number") @Valid Integer number) throws Exception {

        return gitUserService.getWebsites2(number);
    }
}
