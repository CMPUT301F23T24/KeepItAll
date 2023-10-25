package com.example.keepitall;

public class KeepItAll {
  private ArrayList<User> list_of_users;
  public KeepItAll(){
    list_of_users = new ArrayList<User>;
  }

  public add_user(User user){
    list_of_users.add(user);
  }

  public return_array(){
    return list_of_users;
  }
}
