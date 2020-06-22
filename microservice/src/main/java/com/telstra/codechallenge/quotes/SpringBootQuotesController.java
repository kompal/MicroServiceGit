package com.telstra.codechallenge.quotes;

import java.util.Arrays;
import java.util.List;

import com.telstra.codechallenge.errorHandling.CustomExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class SpringBootQuotesController {

  private SpringBootQuotesService springBootQuotesService;

  public SpringBootQuotesController(
      SpringBootQuotesService springBootQuotesService) {
    this.springBootQuotesService = springBootQuotesService;
  }

  @RequestMapping(path = "/quotes", method = RequestMethod.GET)
  public List<Quote> quotes() {
    return Arrays.asList(springBootQuotesService.getQuotes());
  }

  @RequestMapping(path = "/quotes/random", method = RequestMethod.GET)
  public Quote quote() {
    return springBootQuotesService.getRandomQuote();
  }

  @RequestMapping(path ="/user/{number}", method = RequestMethod.GET)
  public List<FindUsers.Item> getWebsites(@PathVariable("number") @Valid Integer number) throws Exception {

    return springBootQuotesService.getWebsites(number);
  }
}
