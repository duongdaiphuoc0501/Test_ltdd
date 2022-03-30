package com.example.danhba;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDAO {
    @Transaction
    @Query("SELECT * FROM contact")
    public List<ConTact> getAllContact();

    @Insert
    public void insertAll(ConTact... contacts);

    @Update
    public void updateContac(ConTact conTact);
}
