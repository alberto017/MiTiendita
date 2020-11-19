package com.example.mitiendita;

import android.content.Context;
import android.content.SharedPreferences;

public class IntroManager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public IntroManager( Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("first",0);
        this.editor = sharedPreferences.edit();
    }//IntroManager

    public void setFirst(boolean isFirst){
        editor.putBoolean("check",isFirst);
        editor.commit();
    }//setFirst

    public boolean Check(){
        return sharedPreferences.getBoolean("check",true);
    }//Check
}//IntroManager
