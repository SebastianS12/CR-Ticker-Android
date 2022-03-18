package com.raffelberg.cr_ticker.persistence;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class Converters {

    @TypeConverter
    public static String listToString(List<String> logs){
        Gson gson = new Gson();
        return gson.toJson(logs);
    }

    @TypeConverter
    public List<String> stringToList(String data){
        Gson gson = new Gson();
        if(data==null){
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<String>>(){}.getType();
        return gson.fromJson(data,listType);
    }

    @TypeConverter
    public String teamToList(Team team){
        Gson gson = new Gson();
        return gson.toJson(team);
    }

    @TypeConverter
    public Team stringToTeam(String data){
        Gson gson = new Gson();
        if(data==null){
            return null;
        }
        Type listType = new TypeToken<Team>(){}.getType();
        return gson.fromJson(data,listType);
    }
}
