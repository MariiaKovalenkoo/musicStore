package com.storeproject.musicstore.model;

import java.io.Serializable;

public class User implements Serializable {
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private Role role;

    public enum Role { SALES, MANAGER }

    public User(String username, String password, String firstName, String lastName, Role role){
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {this.username = username;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public String getFullName(){
        return String.format("%s %s", firstName, lastName);
    }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }
}