package com.example.devblogapplication.model.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareExternalPostRequest {
    private String url;
    private String userThrough;
    private List<String> tags;
}