package com.example.devblogapplication.model.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNewPostRequest {
    private String title;
    private String thumbnail;
    private String content;
    private List<String> tags;
}