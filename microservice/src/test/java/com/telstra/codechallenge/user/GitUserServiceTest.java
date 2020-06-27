package com.telstra.codechallenge.user;

import com.telstra.codechallenge.errorHandling.CustomException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(MockitoJUnitRunner.class)
@EnableConfigurationProperties
@TestPropertySource(properties = "user.base.url=test")
public class GitUserServiceTest {

    @LocalServerPort
    private int port;

    @Value("${user.base.url}")
    public String appDomain;

    @InjectMocks
    @Spy
    GitUserService gitUserService;

    @Autowired
    private RestTemplate restTemplate;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Test
    public void testUserService(){
        exceptionRule.expect(CustomException.class);
        exceptionRule.expectMessage("No users found");
    GitUserService gitUserService = new GitUserService(restTemplate);
    UserModel userModel = new UserModel();
    userModel = gitUserService.getUsers(5);
    if(userModel==null){
        throw new CustomException("No users found","API returned no response",500);
    }

    }

    @Test
    public void testUser() throws Exception{
        String test=gitUserService.getAppDomain();
        System.out.println("test url -"+test);

    }
}
