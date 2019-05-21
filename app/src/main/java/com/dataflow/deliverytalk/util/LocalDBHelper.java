package com.dataflow.deliverytalk.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dataflow.deliverytalk.Models.Alarm;
import com.dataflow.deliverytalk.Models.Carrier;

public class LocalDBHelper extends SQLiteOpenHelper {

    private Context context;

    public LocalDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        this.context = context;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        alarmCreate(db);
        carriersCreate(db);
    }

    private void alarmCreate(SQLiteDatabase db){
        String query =
                "CREATE TABLE alarms (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "type INTEGER(2) NOT NULL," +
                        "parcelId TEXT," +
                        "content TEXT," +
                        "wdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ")";

        db.execSQL(query);
    }
    private void carriersCreate(SQLiteDatabase db){
        String query =
                "CREATE TABLE carriers (" +
                        "id TEXT PRIMARY KEY," +
                        "homepage TEXT," +
                        "name TEXT," +
                        "tel TEXT," +
                        "logo TEXT" +
                        ")";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void testDB(){
        SQLiteDatabase db = getReadableDatabase();
    }

    public void addAlarm(Alarm alarm, SQLiteDatabase db){
        String query = "INSERT INTO ALARMS VALUES(0, ?, ?, ?, 0)";

        db.execSQL(query, new Object[]{ alarm.getType(), alarm.getParcelId(), alarm.getContent() });

    }

    public void insertCarriers(Carrier carrier){
        SQLiteDatabase db = getWritableDatabase();
        String query = "INSERT INTO CARRIERS VALUES(?, ?, ?, ?, ?)";
        db.execSQL(query, new Object[]{carrier.getId(), carrier.getHomepage(), carrier.getName(), carrier.getTel(), carrier.getLogo()});
    }

    public Carrier selectCarrier(String id){
        String query = "SELECT * FROM carriers WHERE id = ?";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cs = db.rawQuery(query, new String[]{id});
        Carrier carrier = new Carrier();
        if(cs.moveToNext()){
            carrier.setId(cs.getString(0));
            carrier.setHomepage(cs.getString(1));
            carrier.setName(cs.getString(2));
            carrier.setTel(cs.getString(3));
            carrier.setLogo(cs.getString(4));

        }
        return carrier;
    }
}
