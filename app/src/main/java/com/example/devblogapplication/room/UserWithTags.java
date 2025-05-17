package com.example.devblogapplication.room;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UserWithTags {

    @Embedded
    private User user;

    @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = @Junction(value = UserTagCrossRef.class,
                    parentColumn = "userId",
                    entityColumn = "tagId")
    )
    private List<Tag> favoriteTags;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Tag> getFavoriteTags() {
        return favoriteTags;
    }

    public void setFavoriteTags(List<Tag> favoriteTags) {
        this.favoriteTags = favoriteTags;
    }
}
