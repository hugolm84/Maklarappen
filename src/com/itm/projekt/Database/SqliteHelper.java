package com.itm.projekt.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;

public class SqliteHelper extends SQLiteOpenHelper {

    private static final String TAG = SqliteHelper.class.getName();
    private static final String TABLE_LOCATIONS = "locations";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_LAT = "_lat";
    private static final String COLUMN_LNG = "_lng";
    private static final String COLUMN_LOCATION = "_location";
    private static final String COLUMN_SUBLOCATION = "_sublocation";
    private static final String COLUMN_HELPER = "_help";

    private static final String DATABASE = "geolocatsdionss.db";
    private static final int DATABASE_VERSION = 2;

    private SQLiteDatabase database;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_LOCATIONS + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_LAT + " REAL not null, "
            + COLUMN_LNG + " REAL not null, "
            + COLUMN_SUBLOCATION + " text not null, "
            + COLUMN_LOCATION + " text not null, "
            + COLUMN_HELPER + " text not null UNIQUE ON CONFLICT replace);";

    public SqliteHelper(Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
    }

    public long createRecord(final String location, final String subLocation, final LatLng latlng) {

        Log.d(TAG, "Creating new record");

        ContentValues values = new ContentValues();
        values.put(COLUMN_LAT, latlng.latitude);
        values.put(COLUMN_LNG, latlng.longitude);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_SUBLOCATION, subLocation);
        values.put(COLUMN_HELPER, location+"/"+subLocation);

        if(database == null)
            database = getWritableDatabase();

        long ret = 0;
        try {
            ret = database.insert(TABLE_LOCATIONS, null, values);
        } catch( SQLiteConstraintException e) {
            Log.d(TAG, "Unable to insert record.");
            e.printStackTrace();
        } finally {
           return ret;
        }
    }

    public Cursor query(final String location) {
        return database.rawQuery(
                "SELECT * FROM " + TABLE_LOCATIONS
                + " WHERE " + COLUMN_LOCATION + " = ? OR "
                + COLUMN_SUBLOCATION + " = ? ", new String[] {location});

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        database = db;
        if(database.needUpgrade(DATABASE_VERSION)) {
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
            Log.d("DATABASE", "DRopped table! Needed upgrade");
        }
        database.execSQL(DATABASE_CREATE);

    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        Log.d(SqliteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        onCreate(database);
    }
}
