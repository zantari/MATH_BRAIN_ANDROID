package com.example.android_math_brain;

import android.content.Context;
import android.content.SharedPreferences;

public class GameData {
    private static final String PREFS_NAME = "lvl_data";
    private static final String KEY_LVL = "current_level";

    private static final int DEFAULT_LEVEL = 1;

    private static GameData instance; // kopia obiektu
    private final Context context; // pamiec
    private int level;

    public GameData(Context context) {
        this.context = context.getApplicationContext();
        loadLevel();
    }



    public int getLevel(){
        return level;
    }
    public void addLevel(){
        level++;
        saveLevel();
    }

    public void setLevel(int newLevel){
        this.level = newLevel;
        saveLevel();
    }



    private void loadLevel(){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.level = prefs.getInt(KEY_LVL, DEFAULT_LEVEL);
    }



    private void saveLevel(){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_LVL, this.level);
        editor.apply();
    }














    public static synchronized GameData getInstance(Context context){
        if(instance == null){
            instance = new GameData(context);
        }
        return instance;
    }

}