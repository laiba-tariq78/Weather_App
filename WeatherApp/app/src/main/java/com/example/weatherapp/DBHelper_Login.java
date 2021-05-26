package com.example.weatherapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper_Login extends SQLiteOpenHelper {
    public static final String DATABASE_NAME ="database.db";



//Table For User

    public static final String User_TABLE ="User";
    public static final String COL_1 ="ID";
    public static final String COL_2 ="UserName";
    public static final String COL_3 ="Password";
    public static final String COL_4 ="Email";



    public DBHelper_Login(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE User (ID INTEGER PRIMARY  KEY AUTOINCREMENT, UserName TEXT, Password TEXT, Email TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int NewVersion) {
        try {

            if (NewVersion > oldVersion) {
                sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + User_TABLE);

                onCreate(sqLiteDatabase);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // Register new User

    public long register(String user, String password, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserName",user);
        contentValues.put("Password",password);
        contentValues.put("Email",email);
        long res = db.insert("User",null,contentValues);
        db.close();
        return  res;
    }

    //Login
    public boolean Login(String email, String password){
        String[] columns = { COL_1 };
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_3 + "=?" + " and " + COL_4 + "=?";
        String[] selectionArgs = {password,email};

        Cursor cursor = db.query(User_TABLE,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }







}
