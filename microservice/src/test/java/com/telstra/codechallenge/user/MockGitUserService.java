package com.telstra.codechallenge.user;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@WebMvcTest(value = GitUserController.class)
public class MockGitUserService {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GitUserService gitUserService;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void getUserTest() throws Exception {
        UserModel userModel = new UserModel();

        Mockito.when(
                gitUserService.getUsers(Mockito.anyInt())).thenReturn(userModel);
        Mockito.when(restTemplate.getForObject(Mockito.anyString(),Mockito.any(), (Object) Mockito.any())).thenReturn(userModel);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/user/3").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse().getStatus());
        Assert.assertNotNull(result);
        Assert.assertEquals(202,result.getResponse().getStatus());
    }

    @Test
    public void getUserTestNegative() throws Exception {
        UserModel userModel = new UserModel();

        Mockito.when(
                gitUserService.getUsers(Mockito.anyInt())).thenReturn(userModel);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/user/103").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());
    }


}
