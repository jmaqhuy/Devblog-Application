package com.example.devblogapplication.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentDTO implements Serializable {
    private Long id;
    private UserDTO user;
    private Long postId;
    private String content;
    private LocalDateTime commentAt;
    private Long parentId;
    private Integer replies;
}
