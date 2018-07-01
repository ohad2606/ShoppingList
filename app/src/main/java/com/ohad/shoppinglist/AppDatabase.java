package com.ohad.shoppinglist;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Item.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ItemDao itemDao();
}