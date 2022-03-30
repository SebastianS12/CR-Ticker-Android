package com.raffelberg.cr_ticker.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;

public class MatchRepository {

    private final MatchDao mMatchDao;

    public MatchRepository(Application application){
        MatchDatabase db = MatchDatabase.getDatabase(application);
        mMatchDao = db.matchDao();
    }

    public void insertMatch(Match match){
        MatchDatabase.databaseWriteExecutor.execute(()->{
            mMatchDao.insertMatch(match);
        });
    }

    public void updateMatch(Match match){
        MatchDatabase.databaseWriteExecutor.execute(()->{
            mMatchDao.updateMatch(match);
        });
    }

    public void deleteMatch(Match match){
        MatchDatabase.databaseWriteExecutor.execute(()->{
            mMatchDao.deleteMatch(match);
        });
    }

    public LiveData<Match> getMatch(String id){
        return mMatchDao.getMatch(id);
    }
}
