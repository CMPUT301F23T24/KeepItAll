/*
* resources:
* https://www.geeksforgeeks.org/remove-element-arraylist-java/amp/
* https://www.tutorialspoint.com/check-existence-of-an-element-in-java-arraylist#:~:text=ArrayList.,the%20element%20is%20not%20present.
* https://stackoverflow.com/questions/7935613/adding-to-an-arraylist-java
*/




package com.example.keepitall;

import java.util.ArrayList;

public class KeepItAll {
  private ArrayList<User> list_of_users;
  public KeepItAll(){
    list_of_users = new ArrayList<User>();
  }

  public void add_user(User user){
    if (list_of_users.contains(user)){
      return;
    }
    list_of_users.add(user);
  }

  public ArrayList<User> get_users(){
    return list_of_users;
  }

  public void delete_user(User user){
    if (!list_of_users.contains(user)){
      return;
    list_of_users.remove(user);
    }

    public int compare_users(User user1, User user2){
      if (user1.getUserName() == user2.getUserName()){
        return 1;
      }

      return 0;
    }
  }
}
