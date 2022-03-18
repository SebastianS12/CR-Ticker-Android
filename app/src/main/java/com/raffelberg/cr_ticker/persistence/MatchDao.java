package com.raffelberg.cr_ticker.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

@Dao
public abstract class MatchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertMatch(Match currentMatch);

    @Update
    abstract void updateMatch(Match currentMatch);

    @Delete
    abstract void deleteMatch(Match match);

    @Transaction
     public void addMatchRoom(Match matchDelete,Match matchInsert){
        deleteMatch(matchDelete);
        insertMatch(matchInsert);
    }

    @Query("SELECT * FROM `Match` Limit 1")
    abstract LiveData<Match>getCurrentMatch();


}
