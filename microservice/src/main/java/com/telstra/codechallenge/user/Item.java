package com.telstra.codechallenge.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Item{
    private String id;
    private String login;
    private String html_url;
}
