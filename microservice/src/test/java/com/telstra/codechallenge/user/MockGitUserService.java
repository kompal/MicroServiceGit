package com.telstra.codechallenge.user;

import java.util.ArrayList;
import java.util.List;

public class MockGitUserService {


    public UserModel getUsers(Integer number){
        UserModel userModel = new UserModel();
        Item item = new Item();
        item.setId("1");
        item.setLogin("test-login");
        item.setHtml_url("test.url.com");
        List<Item> listItems = new ArrayList<>();
        listItems.add(item);
        userModel.setItems(listItems);
        userModel.setItems(listItems);
        return userModel;
    }

}
