package com.telstra.codechallenge.user;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(MockitoJUnitRunner.class)
@EnableConfigurationProperties
@TestPropertySource
public class GitUserControllerTest {

    @InjectMocks
    @Spy
    GitUserService gitUserService;

    @InjectMocks
    @Spy
    GitUserController gitUserController;

    @Mock
    private RestTemplate restTemplate;

    @Value("${user.base.url}")
    public String appDomain;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = standaloneSetup(new GitUserController(gitUserService)).build();
        ReflectionTestUtils.setField(gitUserService, "appDomain", "https://api.github.com/search/users?q=followers:0&sort=joined&order=asc");
    }


    @Test
    public void testControllerBadRequest() throws Exception {

        gitUserController = Mockito.mock(GitUserController.class);
        gitUserService = Mockito.mock(GitUserService.class);
        MockGitUserController mockGitUserController=new MockGitUserController();
        MockGitUserService mockGitUserService = new MockGitUserService();
        UserModel userModel =mockGitUserService.getUsers(5);
        ResponseEntity responseEntity= mockGitUserController.getUsersInavlidRequest(-10);

        Mockito.when(gitUserController.getUsers(anyInt())).thenReturn(responseEntity);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/user/-10").queryParam("number","-10")
                .accept(
                        MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Assert.assertEquals(406,result.getResponse().getStatus());
    }


    @Test
    public void testGitUserControllerValidRequest() throws Exception {

        gitUserController = Mockito.mock(GitUserController.class);
        gitUserService = Mockito.mock(GitUserService.class);

        MockGitUserController mockGitUserController=new MockGitUserController();
        MockGitUserService mockGitUserService = new MockGitUserService();
        UserModel userModel =mockGitUserService.getUsers(5);
        ResponseEntity responseEntity= mockGitUserController.getUsers(5);

        Mockito.when(
                gitUserService.getUsers(Mockito.anyInt())).thenReturn(userModel);
        Mockito.when(gitUserController.getUsers(anyInt())).thenReturn(responseEntity);
        Mockito.when(restTemplate.getForObject(Mockito.anyString(),Mockito.any(), (Object) Mockito.any())).thenReturn(userModel);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/user/5").queryParam("number","5")
                .accept(
                        MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(result.getResponse().getStatus());
        Assert.assertEquals(202,result.getResponse().getStatus());

    }

    @Test
    public void testGitUserControllerInvalidRequest() throws Exception {

        gitUserController = Mockito.mock(GitUserController.class);
        gitUserService = Mockito.mock(GitUserService.class);

        MockGitUserController mockGitUserController=new MockGitUserController();
        MockGitUserService mockGitUserService = new MockGitUserService();
        UserModel userModel =mockGitUserService.getUsers(5);
        ResponseEntity responseEntity= mockGitUserController.getUsers(5);

        Mockito.when(
                gitUserService.getUsers(Mockito.anyInt())).thenReturn(userModel);
        Mockito.when(gitUserController.getUsers(anyInt())).thenReturn(responseEntity);
        Mockito.when(restTemplate.getForObject(Mockito.anyString(),Mockito.any(), (Object) Mockito.any())).thenReturn(userModel);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/user/101").queryParam("number","101")
                .accept(
                        MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(result.getResponse().getStatus());
        Assert.assertEquals(400,result.getResponse().getStatus());

    }



}
