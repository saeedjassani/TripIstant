package com.technovanzahackathon.tripistant;

/**
 * Created by Pooja S on 12/24/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.technovanzahackathon.tripistant.Expense.Expense;
import com.technovanzahackathon.tripistant.TripChecklists.TripChecklists;
import com.technovanzahackathon.tripistant.TripChecklists.Trips;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG=DatabaseHelper.class.getSimpleName();
    private static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="TripDB";
    public SQLiteDatabase database;

    private final Context context;

    DatabaseHelper databaseHelper;

    public static final String TABLE_NOTES="notes";
    public static final String TABLE_PASSCODE = "passcodetable";
    public static final String TABLE_TRIPS = "trips";
    public static final String TABLE_DEFAULT_COLOR = "defaultcolortable";

    public static final String KEY_ID="id";
    public static final String KEY_TITLE="title";
    public static final String KEY_CONTENT="content";
    public static final String KEY_UPDATED_AT="updated_at";
    public static final String KEY_COLOR="color";
    public static final String KEY_TAG="tag";
    public static final String KEY_FAVOURITE="favourite";
    public static final String KEY_TRIP_NAMEC="trip_namec";

    public static final String KEY_LOCKSTATUS = "lock_status";
    public static final String KEY_PASSCODE_ID="passcode_id";
    public static final String KEY_PASSOCDE = "passcode";
    public static final String KEY_PASSCODE_STATUS = "passcode_status";

    public static final String KEY_DEFAULTCOLOR_ID = "default_color_id";
    public static final String KEY_DEFAULT_COLOR = "default_color";
    public static final String KEY_REMINDER_DATE = "reminder_date";
    public static final String KEY_REMINDER_TIME = "reminder_time";
    public static final String KEY_REMINDER_STATUS = "reminder_status";

    public static final String KEY_TRIP_ID="trip_id";
    public static final String KEY_TRIP_NAME = "trip_name";
    public static final String KEY_TRIP_SRC = "trip_src";
    public static final String KEY_TRIP_DEST = "trip_dest";
    public static final String KEY_TRIP_DATE = "trip_date";
    public static final String KEY_TRIP_TIME = "trip_time";
    public static final String KEY_TRIP_DATETIME_STATUS = "trip_datetime_status";

    public static final String TABLE_EXPENSE = "expense";
    public static final String TABLE_BUDGET = "budget";


    public static final String KEY_EXPENSE_ID = "expense_id";
    public static final String KEY_EXPENSE_TITLE = "expense_title";
    public static final String KEY_EXPENSE_NOTE = "expense_note";
    public static final String KEY_AMOUNT = "expense_amount";
    public static final String KEY_EXPENSE_DATE = "expense_date";

    public static final String KEY_BUDGET_ID = "expense_budget_id";
    public static final String KEY_BUDGET = "expense_budget";
    public static final String KEY_BUDGET_STATUS = "expense_budget_status";

    private static final String CREATE_TABLE_NOTE="CREATE TABLE "
            +TABLE_NOTES+"("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +KEY_TITLE+" TEXT, "
            +KEY_CONTENT+" TEXT, "
            +KEY_UPDATED_AT+" TEXT, "
            +KEY_COLOR+" INT, "
            +KEY_LOCKSTATUS+" INT, "
            +KEY_TRIP_NAMEC+" TEXT"+" )";

    private static final String CREATE_TABLE_TRIPS="CREATE TABLE "
            +TABLE_TRIPS+"("+KEY_TRIP_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +KEY_TRIP_NAME+" TEXT, "
            +KEY_TRIP_SRC+" TEXT, "
            +KEY_TRIP_DEST+" TEXT, "
            +KEY_TRIP_DATE+" TEXT, "
            +KEY_TRIP_TIME+" TEXT, "
            +KEY_TRIP_DATETIME_STATUS+" INT"+" )";

    private static final String CREATE_TABLE_PASSCODE="CREATE TABLE IF NOT EXISTS "
            +TABLE_PASSCODE+"("+KEY_PASSCODE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +KEY_PASSOCDE+" TEXT"+" )";

    private static final String CREATE_TABLE_EXPENSE = "CREATE TABLE "
            + TABLE_EXPENSE + "(" + KEY_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_EXPENSE_TITLE + " TEXT, "
            + KEY_EXPENSE_NOTE + " TEXT, "
            + KEY_EXPENSE_DATE + " TEXT, "
            + KEY_AMOUNT + " INT" + " )";

    private static final String CREATE_TABLE_EXPENSE_BUDGET = "CREATE TABLE "
            + TABLE_BUDGET + "(" + KEY_BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_BUDGET + " INT"+" )";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("onCreate","onCreate");
        Log.v("onCreate","onCreate");
        try {
            db.execSQL(CREATE_TABLE_NOTE);
            db.execSQL(CREATE_TABLE_PASSCODE);
            db.execSQL(CREATE_TABLE_TRIPS);
            db.execSQL(CREATE_TABLE_EXPENSE);
            db.execSQL(CREATE_TABLE_EXPENSE_BUDGET);
        }catch(SQLiteException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        
    }

    public void onDowngrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {
        onUpgrade(paramSQLiteDatabase, paramInt1, paramInt2);
    }

    public long createExpenseNote(Expense expense){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EXPENSE_TITLE, expense.getTitle());
        values.put(KEY_EXPENSE_NOTE, expense.getNote());
        values.put(KEY_EXPENSE_DATE, expense.getDate());
        values.put(KEY_AMOUNT, expense.getAmount());
        return db.insert(TABLE_EXPENSE, null, values);
    }

    public void createBudget(float budget){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_BUDGET_ID, 1);
        values.put(KEY_BUDGET, budget);
        db.insert(TABLE_BUDGET, null, values);
    }

    public float getBudget(){
        float result=0;
        SQLiteDatabase db=this.getReadableDatabase();
        String selectQuery="SELECT "+KEY_BUDGET+" FROM "+ TABLE_BUDGET+" WHERE "+KEY_BUDGET_ID+" = 1";
        Log.e(LOG, selectQuery);
        Cursor cursor=db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do {
                result = cursor.getFloat(cursor.getColumnIndex(KEY_BUDGET));
            }while (cursor.moveToNext());
        }
        return result;
    }

    public void updateBudget(float expense) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_BUDGET_ID, 1);
        values.put(KEY_BUDGET, expense);
        db.update(TABLE_BUDGET, values, KEY_BUDGET_ID + " = ?", new String[]{String.valueOf(1)});
    }

    public void deleteBudget(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_BUDGET+" WHERE "+KEY_BUDGET_ID+ " = 1");
    }

    public List<Expense> getAllExpenses() {
        List<Expense> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_EXPENSE;
        Log.e(LOG, selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Expense expense = new Expense();
                    expense.setId(cursor.getLong(cursor.getColumnIndex(KEY_EXPENSE_ID)));
                    expense.setTitle(cursor.getString(cursor.getColumnIndex(KEY_EXPENSE_TITLE)));
                    expense.setNote(cursor.getString(cursor.getColumnIndex(KEY_EXPENSE_NOTE)));
                    expense.setDate(cursor.getString(cursor.getColumnIndex(KEY_EXPENSE_DATE)));
                    expense.setAmount(cursor.getFloat(cursor.getColumnIndex(KEY_AMOUNT)));
                    result.add(expense);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public void updateExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EXPENSE_TITLE, expense.getTitle());
        values.put(KEY_EXPENSE_NOTE, expense.getNote());
        values.put(KEY_EXPENSE_DATE, expense.getDate());
        values.put(KEY_AMOUNT, expense.getAmount());
        db.update(TABLE_EXPENSE, values, KEY_EXPENSE_ID + " = ?", new String[]{String.valueOf(expense.getId())});
    }

    public void deleteExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSE, KEY_EXPENSE_ID + " = ?", new String[]{String.valueOf(expense.getId())});
    }

    public long createTripO(Trips trips){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_TRIP_NAME, trips.getTripname());
        values.put(KEY_TRIP_SRC, trips.getTripsrc());
        values.put(KEY_TRIP_DEST, trips.getTripdest());
        values.put(KEY_TRIP_DATE,trips.getTripdate());
        values.put(KEY_TRIP_TIME, trips.getTriptime());
        values.put(KEY_TRIP_DATETIME_STATUS, trips.getTripdatetimestatus());
        return db.insert(TABLE_TRIPS, null, values);
    }

    public List<Trips> getAllTripsO(){
        List<Trips> result=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        String selectQuery="SELECT * FROM "+TABLE_TRIPS;
        Log.e(LOG, selectQuery);
        Cursor cursor=db.rawQuery(selectQuery, null);
        try{
            if (cursor.moveToFirst()){
                do {
                    Trips note=new Trips();
                    note.setTripId(cursor.getLong(cursor.getColumnIndex(KEY_TRIP_ID)));
                    note.setTripname(cursor.getString(cursor.getColumnIndex(KEY_TRIP_NAME)));
                    note.setTripsrc(cursor.getString(cursor.getColumnIndex(KEY_TRIP_SRC)));
                    note.setTripdest(cursor.getString(cursor.getColumnIndex(KEY_TRIP_DEST)));
                    note.setTripdate(cursor.getString(cursor.getColumnIndex(KEY_TRIP_DATE)));
                    note.setTriptime(cursor.getString(cursor.getColumnIndex(KEY_TRIP_TIME)));
                    note.setTripdatetimestatus(cursor.getInt(cursor.getColumnIndex(KEY_TRIP_DATETIME_STATUS)));
                    result.add(note);
                }while (cursor.moveToNext());
            }
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return result;
    }

    public void updateTripO(Trips trips){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_TRIP_NAME, trips.getTripname());
        values.put(KEY_TRIP_SRC, trips.getTripsrc());
        values.put(KEY_TRIP_DEST, trips.getTripdest());
        values.put(KEY_TRIP_DATE,trips.getTripdate());
        values.put(KEY_TRIP_TIME, trips.getTriptime());
        values.put(KEY_TRIP_DATETIME_STATUS, trips.getTripdatetimestatus());
        db.update(TABLE_TRIPS, values, KEY_TRIP_ID + " = ?", new String[]{String.valueOf(trips.getTripId())});
    }

    public long createTrip(TripChecklists note){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_CONTENT, note.getContent());
        values.put(KEY_UPDATED_AT, note.getUpdated_at());
        values.put(KEY_COLOR,note.getColor());
        values.put(KEY_LOCKSTATUS, note.getLock_status());
        values.put(KEY_TRIP_NAMEC, note.getTripNameC());
        return db.insert(TABLE_NOTES, null, values);
    }

    public void createPasscode(String passcode){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_PASSCODE_ID, 1);
        values.put(KEY_PASSOCDE, passcode);
        db.insert(TABLE_PASSCODE, null, values);
    }

    public boolean isPasscodeSet(){
        boolean set = false;
        SQLiteDatabase db=this.getReadableDatabase();
        String selectQuery="SELECT "+KEY_PASSOCDE+" FROM "+ TABLE_PASSCODE+" WHERE "+KEY_PASSCODE_ID+" = 1";
        Cursor cursor=db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do {
                if(cursor.isNull(cursor.getColumnIndex(KEY_PASSOCDE))){
                    set = false;
                }else if(!cursor.isNull(cursor.getColumnIndex(KEY_PASSOCDE))){
                    set = true;
                }
            }while (cursor.moveToNext());
        }
        return set;
    }

    public String getPasscode(){

        String result="";
        SQLiteDatabase db=this.getReadableDatabase();
        String selectQuery="SELECT "+KEY_PASSOCDE+" FROM "+ TABLE_PASSCODE+" WHERE "+KEY_PASSCODE_ID+" = 1";
        Log.e(LOG, selectQuery);
        Cursor cursor=db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do {
                result = cursor.getString(cursor.getColumnIndex(KEY_PASSOCDE));
            }while (cursor.moveToNext());
        }
        return result;
    }

    public void lockNote(int note_id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("UPDATE "+TABLE_NOTES+" SET "+KEY_LOCKSTATUS+" = 1 WHERE "+KEY_ID+" = "+note_id );
    }

    public void removePasscode(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_PASSCODE+" WHERE "+KEY_PASSCODE_ID+ " = 1");
    }

    public void unlockAllNotes(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "+TABLE_NOTES+" SET "+KEY_LOCKSTATUS+" = 0");
    }

    public void unlockNote(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "+TABLE_NOTES+" SET "+KEY_LOCKSTATUS+" = 0 WHERE "+ KEY_ID+ " = "+id);
    }

    public List<TripChecklists> getAllTrips(String name){
        List<TripChecklists> result=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        String selectQuery="SELECT * FROM "+TABLE_NOTES;
        Log.e(LOG, selectQuery);
        Cursor cursor=db.rawQuery(selectQuery, null);
        try{
            if (cursor.moveToFirst()){
                do {
                    TripChecklists note=new TripChecklists();
                    note.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                    note.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                    note.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
                    note.setUpdated_at(cursor.getString(cursor.getColumnIndex(KEY_UPDATED_AT)));
                    note.setColor(cursor.getInt(cursor.getColumnIndex(KEY_COLOR)));
                    note.setLock_status(cursor.getInt(cursor.getColumnIndex(KEY_LOCKSTATUS)));
                    note.setTripNameC(cursor.getString(cursor.getColumnIndex(KEY_TRIP_NAMEC)));
                    result.add(note);
                }while (cursor.moveToNext());
            }
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return result;
    }

    public void updateTrip(TripChecklists note){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_CONTENT, note.getContent());
        values.put(KEY_UPDATED_AT, note.getUpdated_at());
        values.put(KEY_COLOR,note.getColor());
        values.put(KEY_LOCKSTATUS, note.getLock_status());
        values.put(KEY_TRIP_NAMEC, note.getTripNameC());
        db.update(TABLE_NOTES, values, KEY_ID + " = ?", new String[]{String.valueOf(note.getId())});
    }

    public void deleteTrip(TripChecklists note){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, KEY_ID + " = ?", new String[]{String.valueOf(note.getId())});
    }
}