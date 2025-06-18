package com.example.devblogapplication.model.response;

import com.example.devblogapplication.model.PostDTO;
import com.example.devblogapplication.model.Tag;
import com.example.devblogapplication.model.UserDTO;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchResponse {
    private List<PostDTO> posts;
    private List<UserDTO> users;
    private List<Tag> tags;
}
