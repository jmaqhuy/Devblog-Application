package com.example.devblogapplication.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Meta {
    private String apiVersion;
    private LocalDateTime timestamp;
}
