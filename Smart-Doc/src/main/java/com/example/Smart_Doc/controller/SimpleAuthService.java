package com.example.Smart_Doc.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class SimpleAuthService {

    private final Set<String> loggedInUsers = ConcurrentHashMap.newKeySet();

    public void loginUser(String email) {
        System.out.println("Logging in user: " + email);
        loggedInUsers.add(email);
        System.out.println("Current logged in users: " + loggedInUsers);
    }

    public void logoutUser(String email) {
        System.out.println("Logging out user: " + email);
        loggedInUsers.remove(email);
        System.out.println("Current logged in users after logout: " + loggedInUsers);
    }

    public boolean isUserLoggedIn(String email) {
        boolean isLoggedIn = loggedInUsers.contains(email);
        System.out.println("Checking if user " + email + " is logged in: " + isLoggedIn);
        System.out.println("All logged in users: " + loggedInUsers);
        return isLoggedIn;
    }

    // For debugging 
    public Set<String> getLoggedInUsers() {
        return new HashSet<>(loggedInUsers);
    }

}
