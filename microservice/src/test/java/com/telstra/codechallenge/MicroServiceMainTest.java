package com.telstra.codechallenge;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.telstra.codechallenge.errorHandling.CustomException;
import com.telstra.codechallenge.user.GitUserService;
import com.telstra.codechallenge.user.UserModel;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.telstra.codechallenge.user.UserModel.Item;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MicroServiceMainTest {

  @LocalServerPort
  private int port;

  @Value("${user.base.url}")
  public String appDomain;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void testHealth() throws RestClientException, MalformedURLException {
    System.out.println("PORT NUMBER -"+port);
    ResponseEntity<String> response = restTemplate
        .getForEntity(new URL("http://localhost:" + port + "/actuator/health")
            .toString(), String.class);
    assertEquals("{\"status\":\"UP\"}", response
        .getBody());
  }

}
