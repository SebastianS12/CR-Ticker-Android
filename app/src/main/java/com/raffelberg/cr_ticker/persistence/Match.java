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

    private String state; //Enum?

    private String place;


    private String  date;
    private Team team1;
    private Team team2;
    private List<String> logs;

    public Match(){}

    public Match(String place, String date,Team team1, Team team2){
        id = "Herren_Raffelberg";
        state = "scheduled";
        this.place = place;
        this.date = date;
        this.team1 = team1;
        this.team2 = team2;
        logs = new ArrayList<>();
    }

    public String getState() {
        return state;
    }

    public String getPlace() {
        return place;
    }

    public String getDate() {
        return date;
    }


    public List<String> getLogs(){return logs;}

    public String getId(){
        return id;
    }

    public void setState(String state){
        this.state=state;
    }

    public void setId(String id){
        this.id=id;
    }

    public void setLogs(List<String> logs){
        this.logs=logs;
    }

    public Team getTeam1(){
        return team1;
    }

    public Team getTeam2(){
        return team2;
    }

    public void addLog(String log){logs.add(log);}

    public void setPlace(String place){
        this.place= place;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

}
