package com.telstra.codechallenge.quotes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.telstra.codechallenge.quotes.FindUsers.Item;

import java.util.*;

import org.json.*;

@Service
public class SpringBootQuotesService {

  @Value("${quotes.base.url}")
  private String quotesBaseUrl;

  private RestTemplate restTemplate;

  public SpringBootQuotesService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /**
   * Returns an array of spring boot quotes. Taken from https://spring.io/guides/gs/consuming-rest/.
   *
   * @return - a quote array
   */
  public Quote[] getQuotes() {

    return restTemplate.getForObject(quotesBaseUrl + "/api", Quote[].class);
  }

  /**
   * Returns a random spring boot quote. Taken from https://spring.io/guides/gs/consuming-rest/.
   *
   * @return - a quote
   */
  public Quote getRandomQuote() {
    return restTemplate.getForObject(quotesBaseUrl + "/api/random", Quote.class);
  }

  public List<Item> getWebsites(Integer number){
    //return restTemplate.getForObject("https://api.github.com/search/repositories?q=followers:0&sort=joined&order=asc&limit=3",String.class);
    String ROOT_URI= "https://api.github.com/search/repositories?q=followers:0&sort=joined&order=asc";
    ResponseEntity<String> response = restTemplate.getForEntity(ROOT_URI, String.class);
    System.out.println("response status :"+response.getStatusCode()+"response body"+response.getBody());
    Gson gson = new Gson();
    FindUsers findUsers
            = gson.fromJson(response.getBody(),
            FindUsers.class);
    System.out.println("Find Users with 0 followers-"+ findUsers);

    List<Item> finalresult= new ArrayList<>();
    for(int i=0;i<number;i++){
      Item item = findUsers.getItems().get(i);
      item.setLogin(findUsers.getItems().get(i).getOwner().getLogin());
      finalresult.add(item);
    }

    return finalresult;
  }

}
