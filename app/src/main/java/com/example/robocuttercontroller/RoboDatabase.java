package com.example.robocuttercontroller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import static android.app.DownloadManager.COLUMN_TITLE;

public class RoboDatabase extends SQLiteOpenHelper {

    public Context context;
    public static final String DATABASE_NAME = "robocutter.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "operator";
    public static final String OPERATOR_ID = "op_id";
    public static final String OPERATOR_NAME = "op_name";
    public static final String OPERATOR_TIMEIN = "op_timein";
    public static final String OPERATOR_DATE = "op_date";

     RoboDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable =
                "CREATE TABLE " + TABLE_NAME +
                        "(" + OPERATOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        OPERATOR_NAME + " TEXT, " +
                        OPERATOR_TIMEIN + " TEXT, " +
                        OPERATOR_DATE + " TEXT);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addOperator(String op_name, String op_time, String op_date){
        SQLiteDatabase roboDB = this.getWritableDatabase();
        ContentValues CV = new ContentValues();
        CV.put(OPERATOR_NAME, op_name);
        CV.put(OPERATOR_TIMEIN, op_time);
        CV.put(OPERATOR_DATE, op_date);

       long result =  roboDB.insert(TABLE_NAME, null, CV);
       if(result == -1){
           Toast.makeText(context, "Inserting Unsuccessful", Toast.LENGTH_SHORT).show();
       } else {
           Toast.makeText(context, "Inserting Successful", Toast.LENGTH_SHORT).show();
       }
    }
    Cursor displayOP(){
        String query = "SELECT * FROM "+ TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
           cursor =  db.rawQuery(query, null);
        }
        return cursor;
    }
      void updateData(String row_id, String username, String time, String date){
         SQLiteDatabase db = this.getWritableDatabase();
         ContentValues cv = new ContentValues();
         cv.put(OPERATOR_NAME, username);
        cv.put(OPERATOR_TIMEIN, time);
        cv.put(OPERATOR_DATE, date);

       long result =  db.update(TABLE_NAME, cv,"op_id=?",new String[]{row_id});
       if(result == -1){
           Toast.makeText(context, "Unsuccessfully Update Operator", Toast.LENGTH_SHORT).show();
       } else {
           Toast.makeText(context, "Successfully Update Operator", Toast.LENGTH_SHORT).show();
       }
    }
    void deleteData(String row_id){
         SQLiteDatabase DB = this.getWritableDatabase();
         long result = DB.delete(TABLE_NAME, "op_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Unsuccessfully Delete Operator", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Delete Operator", Toast.LENGTH_SHORT).show();
        }
    }

}
