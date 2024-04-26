package com.eckrin.test.spring_batch.file;

import lombok.Data;

import java.time.Year;

@Data
public class PlayerYear {

    private String id;
    private String lastName;
    private String firstName;
    private String position;
    private int birthYear;
    private int debutYear;
    private int yearsExperience; // 데뷔한지 얼마나 지났는지

    public PlayerYear(Player player) {
        this.id = player.getId();
        this.lastName = player.getLastName();
        this.firstName = player.getFirstName();
        this.position = player.getPosition();
        this.birthYear = player.getBirthYear();
        this.debutYear = player.getDebutYear();
        this.yearsExperience = Year.now().getValue()- player.getDebutYear();
    }
}