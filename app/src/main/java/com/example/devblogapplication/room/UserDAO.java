package com.example.devblogapplication.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Transaction
    @Query("SELECT * FROM User LIMIT 1")
    UserWithTags getUserWithTags();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTags(List<Tag> tags);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserTagCrossRefs(List<UserTagCrossRef> refs);

    @Transaction
    default void updateFavoriteTags(String userId, List<Tag> tags) {
        insertTags(tags); // ignore nếu trùng
        List<UserTagCrossRef> refs = new ArrayList<>();
        for (Tag tag : tags) {
            refs.add(new UserTagCrossRef(userId, tag.getId()));
        }
        insertUserTagCrossRefs(refs);
    }

    @Query("DELETE FROM User")
    void deleteAllUsers();

    @Query("SELECT COUNT(*) FROM User")
    int countUsers();

    @Query("SELECT * FROM User LIMIT 1")
    User getCurrentUser();

    @Query("SELECT * FROM User LIMIT 1")
    LiveData<User> observeCurrentUser();
}
