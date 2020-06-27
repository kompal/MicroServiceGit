package com.telstra.codechallenge.user;


import com.telstra.codechallenge.errorHandling.CustomException;
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
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(MockitoJUnitRunner.class)
@EnableConfigurationProperties
@TestPropertySource
public class GitUserControllerTest {

    @InjectMocks
    @Spy
    GitUserService gitUserService;

    @Mock
    private RestTemplate restTemplate;

    @LocalServerPort
    private int port;

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
        GitUserService gitUserService = Mockito.mock(GitUserService.class);
        GitUserController gitUserController = Mockito.mock(GitUserController.class);

        MockGitUserController mockGitUserController=new MockGitUserController();
        Mockito.when(gitUserController.getUsers(anyInt())).thenReturn(mockGitUserController.getUsers(8));
        UserModel dto = new UserModel();
        gitUserController.setGitUserService(gitUserService);
        when(restTemplate.getForObject("/user/ok", UserModel.class)).thenThrow(new CustomException("Bad Request","Please pass a valid argument (Integer) ",400));
        ResponseEntity<UserModel> response = gitUserController.getUsers(6);
        Assert.assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
    }


    @Test
    public void testUser() throws Exception {

        GitUserController gitUserController = Mockito.mock(GitUserController.class);

        MockGitUserController mockGitUserController=new MockGitUserController();
        Mockito.when(gitUserController.getUsers(anyInt())).thenReturn(mockGitUserController.getUsers(0));

        mockMvc.perform( MockMvcRequestBuilders
                .get("/user").queryParam("number","6")
                .accept(MediaType.APPLICATION_JSON));

    }


}
