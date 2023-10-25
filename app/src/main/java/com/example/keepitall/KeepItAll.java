package com.example.keepitall;

public class KeepItAll {
  private ArrayList<User> list_of_users;
  public KeepItAll(){
    list_of_users = new ArrayList<User>;
  }

  public void add_user(User user){
    list_of_users.add(user);
  }

  public ArrayList<User> return_array(){
    return list_of_users;
  }

  public void delete_user(User user){
    if (!list_of_users(user)){
      return;
    }
  }
}
