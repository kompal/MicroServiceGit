package com.telstra.codechallenge.user;

import org.springframework.web.client.RestTemplate;

public class MockGitUserService {

    private RestTemplate restTemplate;

    public UserModel getUsers(Integer number){
        UserModel userModel = new UserModel();
        return userModel;
    }

}
