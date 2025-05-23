package com.example.devblogapplication.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO implements Serializable {
    private int id;
    private String name;
    private double totalScore;
}
