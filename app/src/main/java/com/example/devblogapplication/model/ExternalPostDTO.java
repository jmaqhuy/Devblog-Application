package com.example.devblogapplication.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExternalPostDTO implements Serializable {
    private String domain;
    private String path;

    private String webLogo;
    private String title;
    private String thumbnail;
}
