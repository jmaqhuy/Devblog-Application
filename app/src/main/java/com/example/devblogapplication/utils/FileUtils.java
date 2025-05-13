package com.example.devblogapplication.utils;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileUtils {

    public static MultipartBody.Part prepareFilePart(Context context, String partName, Uri fileUri) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            String mimeType = contentResolver.getType(fileUri);
            String fileName = getFileNameFromUri(context, fileUri);

            InputStream inputStream = contentResolver.openInputStream(fileUri);
            byte[] bytes = getBytes(inputStream);

            RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), bytes);
            return MultipartBody.Part.createFormData(partName, fileName, requestFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static String getFileNameFromUri(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        String name = "image.jpg"; // fallback
        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (cursor.moveToFirst()) {
                name = cursor.getString(nameIndex);
            }
            cursor.close();
        }
        return name;
    }

}