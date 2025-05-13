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
public class UserInfoDTO {
    private String id;
    private String email;
    private String fullname;
    private String username;
    private String avatarLink;
    private String readme;
    private Integer totalReadingDays;
    private LocalDateTime registrationAt;
    private Set<Tag> favoriteTags;
    private int followers;
    private int following;
}
