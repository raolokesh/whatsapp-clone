package com.lokesh.whatsappclone.Models;

public class User {
    String profilepic,username,email,password,userId,lastMessage;

    public User(){

    }


    // signup constructor
    public User( String username, String email, String password)
    {
        this.username = username;
        this.email = email;
        this.password = password;
    }



    public User(String profilepic, String username, String email, String password, String userId, String lastMessage) {
        this.profilepic = profilepic;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.lastMessage = lastMessage;
    }

    public User(String name, String email) {
        this.username = name;
        this.email = email;

    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
