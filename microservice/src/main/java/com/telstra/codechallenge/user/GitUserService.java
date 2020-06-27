package com.telstra.codechallenge.user;

import com.telstra.codechallenge.errorHandling.CustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class GitUserService {
    private static final Logger LOGGER = LogManager.getLogger(GitUserService.class.getName());

    private RestTemplate restTemplate;

    @Value("${user.base.url}")
    public String appDomain;

    public String getAppDomain() {
        return appDomain;
    }

    public void setAppDomain(String appDomain) {
        this.appDomain = appDomain;
    }

    public GitUserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserModel getUsers(Integer number){

        String ROOT_URI= appDomain;
        Map<String, Integer> params = new HashMap<>();
        URI uri = UriComponentsBuilder.fromUriString(ROOT_URI)
                .buildAndExpand(params)
                .toUri();
        uri = UriComponentsBuilder
                .fromUri(uri)
                .queryParam("per_page", number)
                .build()
                .toUri();
        LOGGER.info("URL"+uri);

        UserModel userModel = restTemplate.getForObject(uri.toString(),UserModel.class,number);
        LOGGER.debug("User list "+userModel);
        if(userModel==null){
            throw new CustomException("No users found","API returned no response",500);
        }
        return  userModel;
    }
}
