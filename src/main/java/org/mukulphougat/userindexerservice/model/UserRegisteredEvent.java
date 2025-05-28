package org.mukulphougat.userindexerservice.model;


import lombok.Data;

@Data
public class UserRegisteredEvent {
    private String id;
    private String username;
    private String email;
    private long timestamp;
}