package com.telstra.codechallenge.user;

import com.google.gson.Gson;
import com.telstra.codechallenge.errorHandling.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import com.telstra.codechallenge.user.UserModel.Item;

import java.util.*;

@Service
public class GitUserService {

    private RestTemplate restTemplate;

    @Autowired
    private Environment environment;

    @Value("${user.base.url}")
    public static String appDomain;

    public void setAppDomain(String appDomain) {
        this.appDomain = appDomain;
    }

    public GitUserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

    }

    public List<Item> getWebsites(Integer number) throws Exception{

        String ROOT_URI= "https://api.github.com/search/repositories?q=followers:0&sort=joined&order=asc&per_page=50";
        ResponseEntity<String> response = restTemplate.getForEntity(ROOT_URI, String.class);
        System.out.println("porp "+appDomain);
        System.out.println("response ENtity -"+response);
        List<Item> finalresult = new ArrayList<>();

        if(response.getStatusCodeValue()==200) {
            Gson gson = new Gson();
            UserModel userModel
                    = gson.fromJson(response.getBody(),
                    UserModel.class);


            if(userModel.getItems().size()>number){ // to avoid null pointer exception
                for (int i = 0; i < number; i++) {
                    Item item = userModel.getItems().get(i);
                    finalresult.add(item);
                }
            }
        }else if(response.getStatusCodeValue()==400){
            throw new CustomException("Bad Request ","Please provide a valid request",response.getStatusCodeValue());
        }else if(response.getStatusCodeValue()==500){
            throw new CustomException("Internal Server Error ","Please try accessing after some time",response.getStatusCodeValue());
        }
        return finalresult;
    }

    public ResponseEntity getWebsites2(Integer number) throws Exception{

        String ROOT_URI= "https://api.github.com/search/repositories?q=followers:0&sort=joined&order=asc&per_page=50";
        ResponseEntity<String> response = restTemplate.getForEntity(ROOT_URI, String.class);
        UserModel userModelResponse = restTemplate.getForObject(ROOT_URI,UserModel.class);
        System.out.println("check ");
        System.out.println("response ENtity -"+response);
        List<Item> finalresult = new ArrayList<>();

        if(response.getStatusCodeValue()==200) {
            Gson gson = new Gson();
            UserModel userModel
                    = gson.fromJson(response.getBody(),
                    UserModel.class);


            if(userModel.getItems().size()>number){ // to avoid null pointer exception
                for (int i = 0; i < number; i++) {
                    Item item = userModel.getItems().get(i);
                    finalresult.add(item);
                }
            }
        }else if(response.getStatusCodeValue()==400){
            throw new CustomException("Bad Request ","Please provide a valid request",response.getStatusCodeValue());
        }else if(response.getStatusCodeValue()==500){
            throw new CustomException("Internal Server Error ","Please try accessing after some time",response.getStatusCodeValue());
        }
        return response;
    }

    public static void main(String args[]) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        String ROOT_URI= "https://api.github.com/search/users?q=followers:0&sort=joined&order=asc&per_page=50";

       UserModel userModelResponse = restTemplate.getForObject(ROOT_URI,UserModel.class);
        System.out.println("get object"+userModelResponse);

    }
}
