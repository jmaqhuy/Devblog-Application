package com.example.devblogapplication.room;

import androidx.room.TypeConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Converters {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @TypeConverter
    public static LocalDateTime fromString(String value) {
        return value == null ? null : LocalDateTime.parse(value, FORMATTER);
    }

    @TypeConverter
    public static String toString(LocalDateTime date) {
        return date == null ? null : date.format(FORMATTER);
    }

}
