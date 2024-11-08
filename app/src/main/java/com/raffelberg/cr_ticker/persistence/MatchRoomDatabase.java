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
public abstract class MatchRoomDatabase extends RoomDatabase {

    public abstract MatchDao matchDao();

    private static volatile MatchRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    static MatchRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MatchRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MatchRoomDatabase.class, "match_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
