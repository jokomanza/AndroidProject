package com.example.sqliteproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.ContentView;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "EsemkaRestourant.db";
    private static final String TABLE_NAME = "MENU_CATEGORY";
    private static final String TABLE_MENU = "MENU";
    private static final int DATABASE_VERSION = 1;

    private Context context;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_MENU + "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        onCreate(db);
    }

    public void AddMenu(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
            Toast.makeText(context, "Failed insert to database", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Success insert to database", Toast.LENGTH_SHORT).show();
    }

    public Cursor GetAllMenu(){
        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if(db != null)
            cursor = db.rawQuery(query,null);

        return cursor;
    }

    public void UpdateMenu(String id, String name){
        //Thread.sleep(5000);
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);

        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.update(TABLE_NAME, contentValues,"id=?", new String[]{id});
        if(result == -1)
            Toast.makeText(context, "Failed update data", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Success update data", Toast.LENGTH_SHORT).show();
    }

    public void DeleteMenu(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "id=?", new String[]{id});

        if(result == -1)
            Toast.makeText(context, "Failed delete data", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Success delete data", Toast.LENGTH_SHORT).show();
    }

    public void DeleteAllMenu() {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("DELETE FROM " + TABLE_NAME);
        onUpgrade(db, 0, 0);

        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
    }
}
