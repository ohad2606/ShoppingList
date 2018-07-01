package com.ohad.shoppinglist;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM item")
    List<Item> getAll();


    @Query("SELECT * from item ORDER BY uid DESC LIMIT 1")
    int getMaxId();

    @Insert
    void insert(Item... items);

    @Delete
    void delete(Item item);

    @Query("DELETE FROM Item")
    void deleteAll();

}

