package com.example.reminderapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    public static final String DB_NAME ="info_users";
    public static int DB_VERSION=2;
    public static final String TABLE_NAME="USERS";
    public static final String PHONE_NO="PHONE_NO";
    public static final String USER_NAME="USER_NAME";

    public static final String TABLE_REMINDER="REMINDERS";
    public static final String REMINDER_NAME="REMINDER_NAME";
    public static final String LATITUDE="LATITUDE";
    public static final String LONGITUDE="LONGITUDE";

    public DBHandler(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE "+TABLE_NAME+" ("+PHONE_NO+" TEXT,"+USER_NAME+" TEXT)";
        db.execSQL(query);



    }

    public void addReminderRecord(String remname, String latitude, String longitude)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(PHONE_NO,SignUp.phone);
        values.put(REMINDER_NAME,remname);
        values.put(LATITUDE,latitude);
        values.put(LONGITUDE,longitude);
        db.insert(TABLE_REMINDER,null,values);
        System.out.println("Records added..");
        db.close();
    }


    public void addNewRecord(String phone)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(PHONE_NO,phone);
        db.insert(TABLE_NAME,null,values);
        System.out.println("Records added..");
        db.close();
    }

    public String findUserName(String phone)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursorPhones=db.rawQuery("SELECT "+USER_NAME+" FROM "+TABLE_NAME+" WHERE "+PHONE_NO+" =? ",new String[]{phone+""});
        String str = null;
        int x;
        if(cursorPhones!=null && cursorPhones.getCount()>0)
        {
            cursorPhones.moveToFirst();
            x=cursorPhones.getColumnIndexOrThrow("USER_NAME");
            str=cursorPhones.getString(x);
            System.out.println("NAME OF THE USER LOGGED IN: "+str);
        }
        cursorPhones.close();
        return str;
    }

    public boolean checkUserName(String phone)
    {

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursorPhones=db.rawQuery("SELECT "+USER_NAME+" FROM "+TABLE_NAME+" WHERE "+PHONE_NO+" =? ",new String[]{phone+""});
        String str = null;
        int x;
        if(cursorPhones!=null && cursorPhones.getCount()>0)
        {
            cursorPhones.moveToFirst();
            x=cursorPhones.getColumnIndexOrThrow("USER_NAME");
            str=cursorPhones.getString(x);
        }
        if((str == null) || str.isEmpty())
        {
            return false;
        }
        cursorPhones.close();
        return true;
    }

    public void setUserName(String phone, String usersname)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues newValues=new ContentValues();
        newValues.put(USER_NAME,usersname);
        db.update(TABLE_NAME,newValues,PHONE_NO+"=?",new String[]{phone});
        db.close();

    }

    public boolean checkRecord(String phone)
    {

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursorPhones=db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+PHONE_NO+" =? ",new String[]{phone+""});
        if(cursorPhones.getCount()>0)
        {
            return true;
        }
        cursorPhones.close();
        return false;
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion<2)
        {
            String query2 = "CREATE TABLE "+TABLE_REMINDER+" ("+REMINDER_NAME+" TEXT,"+LATITUDE+" TEXT,"+LONGITUDE+" TEXT,"+PHONE_NO+" TEXT REFERENCES "+TABLE_NAME+")";
            db.execSQL(query2);
        }
    }


}
