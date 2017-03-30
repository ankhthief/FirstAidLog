package radimbures.firstaidlog;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

public class DBAdapter {

    private static final String TAG = "DBAdapter";

    //database info
    public static final String DATABASE_NAME = "firstaidlog.db";
    public static final String TABLE_EVENTS = "events";  //database name Events
    public static final String TABLE_PARTICIPANTS = "participants"; //database name Participants
    public static final int DATABASE_VERSION = 6;  //database version. Need to increment every time DB changes

    //table Events
    public static final String EVENTS_ROWID = "_id";
    public static final String EVENTS_NAME = "name";

    public static final String[] ALL_KEYS_EVENT = new String[] {EVENTS_ROWID, EVENTS_NAME};

    //table Participants
    public static final String PARTICIPANTS_ROWID = "_id";
    public static final String PARTICIPANTS_NAME = "name";
    public static final String PARTICIPANTS_SURNAME = "surname";
    public static final String PARTICIPANTS_EVENTID = "idevent";

    public static final String[] ALL_KEYS_PARTICIPANT = new String[]  {PARTICIPANTS_ROWID, PARTICIPANTS_NAME, PARTICIPANTS_SURNAME, PARTICIPANTS_EVENTID};

    //SQL to create table Events
    private static final String DATABASE_CREATE_SQL_EVENTS =
            "CREATE TABLE " + TABLE_EVENTS
                    + " (" + EVENTS_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + EVENTS_NAME + " TEXT NOT NULL"
                    + ");";

    //SQL to create table Participants
    private static final String DATABASE_CREATE_SQL_PARTICIPANTS =
            "CREATE TABLE " + TABLE_PARTICIPANTS
                    + " (" + PARTICIPANTS_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + PARTICIPANTS_NAME + " TEXT NOT NULL, "
                    + PARTICIPANTS_SURNAME + " TEXT NOT NULL, "
                    + PARTICIPANTS_EVENTID + " TEXT"
                    + ");";

    private DatabaseHelper myDBHelper;
    public SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        myDBHelper = new DatabaseHelper(ctx);
    }

    //open database
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    //checks if is database empty
    public boolean isEmpty() {
        boolean empty = true;
        Cursor cur = db.rawQuery("SELECT COUNT(*) from "+TABLE_EVENTS, null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt (0) == 0);
        }
        cur.close();

        return empty;
    }

    //checks if is database empty
    public boolean isEmptyParticipants() {
        boolean empty = true;
        Cursor cur = db.rawQuery("SELECT COUNT(*) from "+TABLE_PARTICIPANTS, null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt (0) == 0);
        }
        cur.close();

        return empty;
    }

    //close database
    public void close() {
        myDBHelper.close();
    }

    //returns all data from table Events
    public Cursor getAllRowsEvent() {
        Cursor c = 	db.query(true, TABLE_EVENTS, ALL_KEYS_EVENT, null, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    //returns all data from table Participants
    public Cursor getAllRowsParticipant(long radek) {
        String S = String.valueOf(radek);
        Cursor c = db.query(true, TABLE_PARTICIPANTS, ALL_KEYS_PARTICIPANT,"idevent=?",new String[] {S}, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    //creates dataset to add to the table Events
    public long insertRowEvent(String name) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(EVENTS_NAME, name);

        //add dataset to table Events
        return db.insert(TABLE_EVENTS, null, initialValues);
    }

    //creates dataset to add to the table Participants
    public long insertRowParticipant(String name, String surname, Long idevent) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PARTICIPANTS_NAME, name);
        initialValues.put(PARTICIPANTS_SURNAME, surname);
        initialValues.put(PARTICIPANTS_EVENTID, idevent);

        //add dataset to table Participants
        return db.insert(TABLE_PARTICIPANTS, null, initialValues);
    }

    //deletes row from db by EVENT_ROWID
    public boolean deleteRowEvent(long rowId) {
        String where = EVENTS_ROWID + "=" + rowId;
        return db.delete(TABLE_EVENTS, where, null) != 0;
    }

    //deletes row from db by PARTICIPANT_ROWID
    public boolean deleteRowParticipant(long rowId) {
        String where = PARTICIPANTS_ROWID + "=" + rowId;
        return db.delete(TABLE_PARTICIPANTS, where, null) != 0;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {

            _db.execSQL(DATABASE_CREATE_SQL_EVENTS);
            _db.execSQL(DATABASE_CREATE_SQL_PARTICIPANTS);

        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Zničení staré db:
            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPANTS);

            // Znovuvytvoření db:
            onCreate(_db);
        }
    }
}
