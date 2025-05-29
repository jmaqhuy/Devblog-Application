package com.example.devblogapplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TagWithScore {
    private int id;
    private String name;
    private String description;
    private double totalScore;
    private long postCount;
    private boolean isFavorite;
}
