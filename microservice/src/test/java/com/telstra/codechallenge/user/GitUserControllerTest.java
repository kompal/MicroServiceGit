package com.telstra.codechallenge.user;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(MockitoJUnitRunner.class)
@EnableConfigurationProperties
//@TestPropertySource(properties = "user.base.url=test")
public class GitUserControllerTest {
    @InjectMocks
    @Spy
    GitUserController gitUserController;

    @InjectMocks
    @Spy
    GitUserService gitUserService;

    @Mock
    private RestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Value("${user.base.url}")
    public String appDomain;

    ResponseEntity responseEntity = mock(ResponseEntity.class);

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = standaloneSetup(new GitUserController(gitUserService)).build();
        ReflectionTestUtils.setField(gitUserService, "appDomain", "https://api.github.com/search/users?q=followers:0&sort=joined&order=asc");
    }

    @Test
    public void testController() throws Exception {

        System.out.println("appdomain-"+appDomain);
        appDomain = "https://api.github.com/search/users?q=followers:0&sort=joined&order=asc";
        UserModel dto = new UserModel();
        ResponseEntity<UserModel> responseEntity = new ResponseEntity<>(dto,HttpStatus.ACCEPTED);
        ReflectionTestUtils.setField(gitUserService, "appDomain", "https://api.github.com/search/users?q=followers:0&sort=joined&order=asc");
        //GitUserService gitUserService = new GitUserService(restTemplate);
        gitUserController.setGitUserService(gitUserService);
        when(restTemplate.getForObject("http://localhost:8080/user/6", ResponseEntity.class)).thenReturn(responseEntity);
        //when(gitUserService.getUsers(anyInt())).thenReturn(mockGitUserService.getUsers(3));
        ResponseEntity<UserModel> response = gitUserController.getUsers(6);
        Assert.assertEquals(response.getStatusCode(),HttpStatus.OK);

        //
        //@TestPropertySource("classpath:application.properties")
    }

    @Test
    public void testControllerBadRequest() throws Exception {
        appDomain = "https://api.github.com/search/users?q=followers:0&sort=joined&order=asc"; // passing wrong url
        UserModel dto = new UserModel();
        GitUserService gitUserService = new GitUserService(restTemplate);
        gitUserController.setGitUserService(gitUserService);
        when(restTemplate.getForObject("http://localhost:8080/user2/6", UserModel.class)).thenReturn(dto);
        ResponseEntity<UserModel> response = gitUserController.getUsers(6);
        Assert.assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
    }

    @Test @Ignore
    public void testservice() throws Exception{
        Mockito.when(restTemplate.getForObject(
                Mockito.anyString(),
                ArgumentMatchers.any(Class.class)
        ))
                .thenReturn(responseEntity);
        UserModel userModel  = gitUserService.getUsers(9);
        Assert.assertNotNull(userModel);

    }

    @Test
    public void testControllerBadRquest() throws Exception {
        appDomain = "https://api.github.com/search/users?q=followers:0&sort=joined&order=asc"; // passing wrong url
        ReflectionTestUtils.setField(gitUserService, "appDomain", "https://api.github.com/search/users?q=followers:0&sort=joined&order=asc");
        UserModel dto = new UserModel();
        GitUserService gitUserService = new GitUserService(restTemplate);
        gitUserController.setGitUserService(gitUserService);
        when(restTemplate.getForObject("http://localhost:8080/user2/6", UserModel.class)).thenReturn(dto);
        ResponseEntity<UserModel> response = gitUserController.getUsers(6);
        Assert.assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testUser() throws Exception{
        String test=gitUserService.getAppDomain();
        System.out.println("test url -"+test);

    }
}
