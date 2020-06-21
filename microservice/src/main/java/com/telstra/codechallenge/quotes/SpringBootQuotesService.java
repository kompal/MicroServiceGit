package com.telstra.codechallenge.quotes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  public List<ZeroFollowers.Item> getWebsites(Integer number){
    //return restTemplate.getForObject("https://api.github.com/search/repositories?q=followers:0&sort=joined&order=asc&limit=3",String.class);
    String ROOT_URI= "https://api.github.com/search/repositories?q=followers:0&sort=joined&order=asc";
    //String jsontest = restTemplate.getForObject("https://api.github.com/search/repositories?q=followers:0&sort=joined&order=asc",String.class);
    //System.out.println("result 1 = "+jsontest);
    ResponseEntity<String> response = restTemplate.getForEntity(ROOT_URI, String.class);
    System.out.println("response 2 -->"+response.getBody());
    JSONObject json = new JSONObject(response.getBody());
    Gson gson = new Gson();
    ZeroFollowers zeroFollowers
            = gson.fromJson(response.getBody(),
            ZeroFollowers.class);
    System.out.println("Zerofllowers-"+zeroFollowers);

    List<ZeroFollowers.Item> finalresult= new ArrayList<>();
    for(int i=0;i<number;i++){
      finalresult.add(zeroFollowers.getItems().get(i));
    }
    return finalresult;
  }

  public static void main(String args[]) throws JsonProcessingException {
    String ROOT_URI= "https://api.github.com/search/repositories?q=followers:0&sort=joined&order=asc";
    Map<String,String> mapuri = new HashMap<>();
    mapuri.put("followers","0");
    mapuri.put("sort","joined");
    mapuri.put("order","asc");
    System.out.println("map check ->"+mapuri);
    RestTemplate restTemplate2 = new RestTemplate(); //https://google.com/search?q=java
    /*HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    HttpEntity <String> entity = new HttpEntity<String>(headers);
    return restTemplate.exchange(" http://localhost:8080/products", HttpMethod.GET, entity, String.class).getBody();
*/
    String jsontest = restTemplate2.getForObject("https://api.github.com/search/repositories?q=followers:0&sort=joined&order=asc",String.class);
    System.out.println("result 1 = "+jsontest);
    ResponseEntity<String> response = restTemplate2.getForEntity(ROOT_URI, String.class);
    System.out.println("response 2 -->"+response.getBody());
    JSONObject json = new JSONObject(response.getBody());
    System.out.println("JSON OBJECT -"+json);
    System.out.println(json.get("items"));
    Gson gson = new Gson();
    ZeroFollowers zeroFollowers
            = gson.fromJson(response.getBody(),
            ZeroFollowers.class);
    System.out.println("Zerofllowers-"+zeroFollowers);

    List<ZeroFollowers.Item> finalresult= new ArrayList<>();
    for(int i=0;i<5;i++){
      finalresult.add(zeroFollowers.getItems().get(i));
    }

    for(ZeroFollowers.Item item : finalresult){
      System.out.println(item);
    }
    //String jsonresp = restTemplate2.getForObject("https://api.github.com/search/repositories",String.class,mapuri);
    //ResponseEntity<String> obj = restTemplate2.getForEntity("https://api.github.com/search/repositories",String.class,mapuri);
    //System.out.println("result 2 = "+obj);
  }
}
