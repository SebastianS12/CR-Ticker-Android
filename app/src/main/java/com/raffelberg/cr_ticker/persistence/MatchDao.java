package com.raffelberg.cr_ticker.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public abstract class MatchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertMatch(Match currentMatch);

    @Update
    abstract int updateMatch(Match currentMatch);

    @Delete
    abstract void deleteMatch(Match match);

    @Query("SELECT * FROM `Match` WHERE id = :id Limit 1")
    abstract LiveData<Match> getMatch(String id);

    @Query("SELECT COUNT() FROM `Match` WHERE id = :id ")
    abstract int matchExists(String id);
}
