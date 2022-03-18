package com.raffelberg.cr_ticker.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.raffelberg.cr_ticker.persistence.Match;
import com.raffelberg.cr_ticker.persistence.MatchRepository;

public class MatchViewModel extends AndroidViewModel {

    private MatchRepository mRepository;


    public MatchViewModel(Application application) {
        super(application);
        mRepository = new MatchRepository(application);
    }

    public void insertMatch(Match match){
        mRepository.insertMatch(match);
    }

    public void updateMatch(Match match){mRepository.updateMatch(match);}

    public void deleteMatch(Match match){mRepository.deleteMatch(match);}

    public void addMatchRoom(Match matchDelete, Match matchInsert){
        mRepository.addMatchRoom(matchDelete,matchInsert);
    }

    //returns match from local database(there's only one)
    public LiveData<Match> getCurrentMatch() {
        return mRepository.getCurrentMatch();
    }



    }


