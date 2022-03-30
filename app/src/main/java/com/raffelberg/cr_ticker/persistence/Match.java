package com.raffelberg.cr_ticker.persistence;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Match {

    @PrimaryKey
    @NonNull
    private String id;

    private String state;
    private String place;
    private String date;
    private List<String> logs;

    private Team team1;
    private Team team2;

    public Match(){}

    public Match(@NonNull String id, String place, String date, Team team1, Team team2){
        this.id = id;
        this.state = MatchStates.scheduled.name();
        this.place = place;
        this.date = date;
        this.team1 = team1;
        this.team2 = team2;
        logs = new ArrayList<>();
    }

    @NonNull
    public String getId(){return this.id;}
    public void setId(@NonNull String id){this.id = id;}

    public String getState(){return this.state;}
    public void setState(String state){this.state = state;}

    public String getPlace(){return this.place;}
    public void setPlace(String place){this.place = place;}

    public String getDate(){return this.date;}
    public void setDate(String date){this.date = date;}

    public Team getTeam1() {return this.team1;}
    public void setTeam1(Team team1){this.team1 = team1;}

    public Team getTeam2() {return this.team2;}
    public void setTeam2(Team team2){this.team2 = team2;}

    public List<String> getLogs(){return this.logs;}
    public void setLogs(List<String> logs){this.logs = logs;}
}
