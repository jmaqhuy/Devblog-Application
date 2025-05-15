package com.example.devblogapplication.model;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private UserDTO author;
    private String title;
    private String thumbnail;
    private String content;
    private ExternalPostDTO externalPost;
    private Set<TagDTO> tags;
    private LocalDateTime publicationDate;
    private boolean isLiked;
    private Integer likes;
    private Integer comments;
    private boolean isDisliked = false;
}
