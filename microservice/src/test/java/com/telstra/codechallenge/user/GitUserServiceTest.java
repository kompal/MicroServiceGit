package com.telstra.codechallenge.user;

import com.telstra.codechallenge.errorHandling.CustomException;
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
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(MockitoJUnitRunner.class)
@EnableConfigurationProperties
@TestPropertySource
public class GitUserServiceTest {

    @LocalServerPort
    private int port;

    @Value("${user.base.url}")
    public String appDomain;

    @InjectMocks
    @Spy
    GitUserService gitUserService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    @Spy
    GitUserController gitUserController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = standaloneSetup(new GitUserController(gitUserService)).build();
        ReflectionTestUtils.setField(gitUserService, "appDomain", "https://api.github.com/search/users?q=followers:0&sort=joined&order=asc");
    }

    @Test (expected = CustomException.class)
    public void testUserLimitExceeds() throws Exception {

        gitUserService.setRestTemplate(restTemplate);
        gitUserController.setGitUserService(gitUserService);
        UserModel userModel = new UserModel();
        Mockito.when(
                gitUserService.getUsers(Mockito.anyInt())).thenThrow(new CustomException("Page can display 100 records at a time, you've exceeded the max user retrieval limit","limit 100 users",400));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/user/102").queryParam("number","102")
                .accept(
                MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    }

}
