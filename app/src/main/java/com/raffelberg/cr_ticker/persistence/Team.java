package com.raffelberg.cr_ticker.persistence;

public class Team {

    private String teamName;
    private String teamScore;

    public Team(){};

    public Team(String teamName){
        this.teamName = teamName;
        this.teamScore = "0";
    }

    public String getTeamName(){return this.teamName;}
    public void setTeamName(String teamName){this.teamName = teamName;}

    public String getTeamScore(){return this.teamScore;}
    public void setTeamScore(String teamScore){this.teamScore = teamScore;}
}
