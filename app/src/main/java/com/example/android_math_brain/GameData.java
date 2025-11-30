package com.example.android_math_brain;

import android.content.Context;
import android.content.SharedPreferences;

public class GameData {
    private static final String PREFS_NAME = "lvl_data";
    private static final String KEY_LVL = "current_level";
    private static final String KEY_POINTS = "current_points";

    private static final int DEFAULT_LEVEL = 1;
    private static final int DEFAULT_POINTS = 0;


    private static GameData instance;
    private final Context context;
    private int level;
    private int points;
    public GameData(Context context) {
        this.context = context.getApplicationContext();
        loadLevel();
        loadPoints();

    }



    public int getLevel(){
        return level;
    }
    public int getPoints() {
        return points;
    }
    public void addLevel(){
        level++;
        saveLevel();
    }
    public void addPoints(int pointsToAdd){
        this.points += pointsToAdd;
        savePoints();
    }

    public void setLevel(int newLevel){
        this.level = newLevel;
        saveLevel();
    }

    public void setPoints(int newPoints) {
        this.points = newPoints;
        savePoints();
    }



    private void loadLevel(){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.level = prefs.getInt(KEY_LVL, DEFAULT_LEVEL);
    }
    private void loadPoints(){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.points = prefs.getInt(KEY_POINTS, DEFAULT_POINTS);
    }



    private void saveLevel(){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_LVL, this.level);
        editor.apply();
    }

    private void savePoints() {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_POINTS, this.points);
        editor.apply();
    }

    public static synchronized GameData getInstance(Context context){
        if(instance == null){
            instance = new GameData(context);
        }
        return instance;
    }

}
