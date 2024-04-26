package com.eckrin.test.spring_batch.file;

import lombok.Data;

@Data
public class Player{

    private String id;
    private String lastName;
    private String firstName;
    private String position;
    private int birthYear;
    private int debutYear;
}