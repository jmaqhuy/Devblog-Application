package com.example.devblogapplication.model.response;

import com.example.devblogapplication.model.PostDTO;
import com.example.devblogapplication.model.TagWithScore;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagDetailResponse {
    private TagWithScore tag;
    private List<PostDTO> posts;
}
