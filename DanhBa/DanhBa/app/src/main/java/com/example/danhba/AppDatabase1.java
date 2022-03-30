package com.example.danhba;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ConTact.class}, version = 1)
public abstract class AppDatabase1 extends RoomDatabase {
    public abstract ContactDAO ConTactDAO();

    private static AppDatabase1 instance;

    public static AppDatabase1 getInstance(Context context){
        if(instance == null)
            instance = Room.databaseBuilder(context,
                    AppDatabase1.class, "contactapp").build();
        return instance;
    }
}
