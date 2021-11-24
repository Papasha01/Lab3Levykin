package com.example.lab3levykin;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DB extends SQLiteOpenHelper {

    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE KeySettings (id INT, keys TXT);";
        db.execSQL(sql);

        sql = "CREATE TABLE SavedForecast (id INT, date TXT,city TXT,temprature FLOAT, wind FLOAT, pressure FLOAT, precip FLOAT,hum INT, cloud INT);";
        db.execSQL(sql);
    }

    public int getMaxIDForKey(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT Max(id) FROM KeySettings";
        Cursor cur = db.rawQuery(sql, null);
        if (cur.moveToFirst()){
            int maxid = cur.getInt(0);
            cur.close();
            return maxid;
        }
        cur.close();
        return 0;
    }

    public void addKey(int id, String key)
    {
        String sid = String.valueOf(id);
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO KeySettings VALUES ("+sid+", '"+key+"');";
        db.execSQL(sql);
    }

    public void getAllKeys(ArrayList<Keys> lst)
    {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT id,keys FROM KeySettings;";
        Cursor cur = db.rawQuery(sql,null);
        if(cur.moveToFirst()){
            do {
                Keys n = new Keys();
                n.id = cur.getInt(0);
                n.Key = cur.getString(1);
                lst.add(n);
            } while (cur.moveToNext());
        }
        cur.close();
    }

    public int getMaxID(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT Max(id) FROM SavedForecast";
        Cursor cur = db.rawQuery(sql, null);
        if (cur.moveToFirst()){
            int maxid = cur.getInt(0);
            cur.close();
            return maxid;
        }
        cur.close();
        return 0;
    }

    public void addForecast(int id, String date,String city,float temperature,float wind,float pressure,float precip,int hum,int cloud)
    {
        String sid = String.valueOf(id);
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO SavedForecast VALUES ("+sid+",'"+date+"','"+city+"','"+temperature+"','"+wind+"','"+pressure+"','"+precip+"','"+hum+"','"+cloud+"');";
        db.execSQL(sql);
    }

    public void getAllForecast(ArrayList<Forecast> lst)
    {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT id,date,city,temprature,wind,pressure,precip,hum,cloud FROM SavedForecast;";
        Cursor cur = db.rawQuery(sql,null);
        if(cur.moveToFirst()){
            do {
                Forecast n = new Forecast();
                n.id = cur.getInt(0);
                n.date = cur.getString(1);
                n.city = cur.getString(2);
                n.temp = cur.getFloat(3);
                n.wind = cur.getFloat(4);
                n.pressure = cur.getFloat(5);
                n.precipitation = cur.getFloat(6);
                n.hum = cur.getInt(7);
                n.cloud = cur.getInt(8);
                lst.add(n);
            } while (cur.moveToNext());
        }
        cur.close();
    }

    public void deleteAll()
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM SavedForecast";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
