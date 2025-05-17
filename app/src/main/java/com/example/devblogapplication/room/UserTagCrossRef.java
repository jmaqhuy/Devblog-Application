package com.example.devblogapplication.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity(
        primaryKeys = {"userId", "tagId"},
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Tag.class,
                        parentColumns = "id",
                        childColumns = "tagId",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class UserTagCrossRef {
    @NonNull
    private String userId;
    @NonNull
    private Integer tagId;

    @NonNull
    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(@NonNull Integer tagId) {
        this.tagId = tagId;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }
}
