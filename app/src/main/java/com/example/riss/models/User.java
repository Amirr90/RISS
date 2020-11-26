package com.example.riss.models;

public class User {

    private boolean isAadharVerified;
    private String email;
    private String firstName;
    private String lastName;
    private String mobile;
    private String address;
    private String username;
    private String image;
    private String lastSignInTime;


    public boolean isAadharVerified() {
        return isAadharVerified;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }

    public String getUsername() {
        return username;
    }

    public String getImage() {
        return image;
    }

    public String getLastSignInTime() {
        return lastSignInTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "isAadharVerified=" + isAadharVerified +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", address='" + address + '\'' +
                ", username='" + username + '\'' +
                ", image='" + image + '\'' +
                ", lastSignInTime='" + lastSignInTime + '\'' +
                '}';
    }
}
