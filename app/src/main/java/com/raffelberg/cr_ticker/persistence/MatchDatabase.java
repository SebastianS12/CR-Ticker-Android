package com.raffelberg.cr_ticker.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Match.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MatchDatabase extends RoomDatabase{
    public abstract MatchDao matchDao();

    private static final String DATABASE_NAME = "match_database";

    private static volatile MatchDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static MatchDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (MatchDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MatchDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
                }
            }
        }

        return INSTANCE;
    }
}
