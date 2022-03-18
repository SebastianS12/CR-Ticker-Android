package com.raffelberg.cr_ticker.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;

public class MatchRepository {

    private MatchDao mMatchDao;


    public MatchRepository(Application application){
        MatchRoomDatabase db = MatchRoomDatabase.getDatabase(application);
        mMatchDao = db.matchDao();

    }

    public void insertMatch(Match match){
        MatchRoomDatabase.databaseWriteExecutor.execute(()->{
            mMatchDao.insertMatch(match);
        });

    }

    public void updateMatch(Match match){
        MatchRoomDatabase.databaseWriteExecutor.execute(()->{
            mMatchDao.updateMatch(match);
        });
    }

    public void deleteMatch(Match match){
        MatchRoomDatabase.databaseWriteExecutor.execute(()->{
            mMatchDao.deleteMatch(match);
        });
    }

    public void addMatchRoom(Match matchDelete, Match matchInsert){
        MatchRoomDatabase.databaseWriteExecutor.execute(()->{
            mMatchDao.addMatchRoom(matchDelete,matchInsert);
        });

    }

    public LiveData<Match> getCurrentMatch() {

        return mMatchDao.getCurrentMatch();

    }


}
