package com.raffelberg.cr_ticker.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MatchViewModel extends ViewModel {
    private final MatchRepository matchRepository;

    public MatchViewModel(Application application){
        matchRepository = new MatchRepository(application);
    }

    public void insertMatch(Match match){matchRepository.insertMatch(match);}
    public void updateMatch(Match match){matchRepository.updateMatch(match);}
    public void deleteMatch(Match match){matchRepository.deleteMatch(match);}
    public LiveData<Match> getMatch(String id){return matchRepository.getMatch(id);}
    public int matchExists(String id){return matchRepository.matchExists(id);}
}
