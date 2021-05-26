package com.example.weatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "WeatherApp.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Cities(Name TEXT PRIMARY KEY, Status INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("drop Table if exists Cities");
    }

    public Boolean InsertCity(String Name, int Status)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", Name);
        contentValues.put("Status", Status);
        long Results = DB.insert("Cities",null, contentValues);
        if(Results == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Boolean UpdateCity(String Name, int Status)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Status", 0);
        Cursor cursor = DB.rawQuery("Select * from Cities where name = ?", new String[] {Name});
        if(cursor.getCount() > 0){
            DB.update("Cities", contentValues,null,null);
            contentValues.put("Status", Status);
            long Results = DB.update("Cities", contentValues, "name=?", new String[] {Name});
            if(Results == -1){
                return false;
            }
            else{
                return true;
            }
        }
        else{
            return false;
        }
    }

    public Boolean DeleteCity(String Name)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor = DB.rawQuery("Select * from Cities where name = ?", new String[] {Name});
        if(cursor.getCount() > 0){
            long Results = DB.delete("Cities","name=?", new String[] {Name});
            if(Results == -1){
                return false;
            }
            else{
                cursor = GetCities();
                if(cursor.getCount() > 0){
                    Cursor tmp = DB.rawQuery("Select * from Cities where Status=1",null);
                    if(!(tmp.getCount() > 0)){
                        while(cursor.moveToNext()){
                            UpdateCity(cursor.getString(0),1);
                            break;
                        }
                    }
                }
                return true;
            }
        }
        else{
            return false;
        }
    }

    public Cursor GetCities()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Cities ", null);
        return cursor;
    }

    public String GetDefaultCity()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Cities where Status=1", null);
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                return cursor.getString(0);
            }
            return "";
        }
        else {
            return "";
        }
    }
}
