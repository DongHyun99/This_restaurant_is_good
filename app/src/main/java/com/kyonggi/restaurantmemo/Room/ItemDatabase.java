package com.kyonggi.restaurantmemo.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kyonggi.restaurantmemo.Data.Item;

@Database(entities = {Item.class}, version = 1)
public abstract class ItemDatabase extends RoomDatabase {

    private static ItemDatabase INSTANCE = null;
    private static String DB_NAME = "item_db";
    public abstract ItemDAO itemDAO();

    //객체 생성
    public synchronized static ItemDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ItemDatabase.class, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    // 객체 제거
    public static void destroyInstance() {
        INSTANCE = null;
    }
}
