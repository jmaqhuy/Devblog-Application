package com.example.devblogapplication.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {User.class, Tag.class, UserTagCrossRef.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class DevblogDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "devblog_database";
    private static DevblogDatabase instance;

    public static synchronized DevblogDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    DevblogDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract UserDAO userDAO();
}
