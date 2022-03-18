package com.raffelberg.cr_ticker.persistence;

public class Team {
    private String team_name;

    private String team_score;

    public Team(){}

    public Team(String team_name){
        this.team_name = team_name;
        this.team_score = "0";
    }

    public String getTeam_name() {
        return team_name;
    }

    public String getTeam_score() {
        return team_score;
    }

    public void setTeam_score(String team_score){
        this.team_score=team_score;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }
}
