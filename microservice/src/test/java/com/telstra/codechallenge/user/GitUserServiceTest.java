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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(MockitoJUnitRunner.class)
@EnableConfigurationProperties
@TestPropertySource
public class GitUserServiceTest {

    @Value("${user.base.url}")
    public String appDomain;

    @InjectMocks
    @Spy
    GitUserService gitUserService;

    @Mock
    private RestTemplate restTemplate;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = standaloneSetup(new GitUserController(gitUserService)).build();
        ReflectionTestUtils.setField(gitUserService, "appDomain", "https://api.github.com/search/users?q=followers:0&sort=joined&order=asc");
    }

    @Test
    public void TestGitUserServiceResponse() throws Exception {
         gitUserService = Mockito.mock(GitUserService.class);
        MockGitUserController mockGitUserController=new MockGitUserController();
        MockGitUserService mockGitUserService = new MockGitUserService();
        UserModel userModel =mockGitUserService.getUsers(5);

        Mockito.when(
                gitUserService.getUsers(Mockito.anyInt())).thenReturn(userModel);
        Mockito.when(restTemplate.getForObject(Mockito.anyString(),Mockito.any(), (Object) Mockito.any())).thenReturn(userModel);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/user/10").queryParam("number","10")
                .accept(
                        MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();


        Assert.assertEquals(202,result.getResponse().getStatus());
        System.out.println(result.getResponse().getStatus());
    }

    @Test
    public void TestGitUserServiceBadRequest() throws Exception {
        GitUserService gitUserService = Mockito.mock(GitUserService.class);
        MockGitUserService mockGitUserService = new MockGitUserService();
        UserModel userModel =mockGitUserService.getUsers(5);

        Mockito.when(
                gitUserService.getUsers(Mockito.anyInt())).thenReturn(userModel);
        Mockito.when(restTemplate.getForObject(Mockito.anyString(),Mockito.any(), (Object) Mockito.any())).thenThrow( new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/user/-8").queryParam("number","-8")
                .accept(
                        MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        result.getResponse().getErrorMessage();
        System.out.println(result.getResponse().getStatus());
        Assert.assertEquals(406,result.getResponse().getStatus());

    }

    @Test
    public void TestGitUserServiceServerError() throws Exception {
         gitUserService = Mockito.mock(GitUserService.class);
        MockGitUserService mockGitUserService = new MockGitUserService();
        UserModel userModel =mockGitUserService.getUsers(5);

        Mockito.when(
                gitUserService.getUsers(Mockito.anyInt())).thenReturn(userModel);
        Mockito.when(restTemplate.getForObject(Mockito.anyString(),Mockito.any(), (Object) Mockito.any())).thenThrow( new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/user/10").queryParam("number","10")
                .accept(
                        MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(result.getResponse().getStatus());
        Assert.assertEquals(500,result.getResponse().getStatus());

    }
}
