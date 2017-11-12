package radimbures.firstaidlog;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

public class DBAdapter {

    private static final String TAG = "DBAdapter";

    //database info
    private static final String DATABASE_NAME = "firstaidlog.db";
    private static final String TABLE_EVENTS = "events";  //database name Events
    private static final String TABLE_PARTICIPANTS = "participants"; //database name Participants
    private static final String TABLE_REGISTR = "registr"; //database name Registr
    private static final String TABLE_INJURIES = "injuries";
    private static final int DATABASE_VERSION = 7;  //database version. Need to increment every time DB changes

    //table Events
    private static final String EVENTS_ROWID = "_id";
    public static final String EVENTS_NAME = "name";

    private static final String[] ALL_KEYS_EVENT = new String[] {EVENTS_ROWID, EVENTS_NAME};

    //table Participants
    private static final String PARTICIPANTS_ROWID = "_id";
    public static final String PARTICIPANTS_NAME = "name";
    public static final String PARTICIPANTS_SURNAME = "surname";

    private static final String[] ALL_KEYS_PARTICIPANT = new String[]  {PARTICIPANTS_ROWID, PARTICIPANTS_NAME, PARTICIPANTS_SURNAME};

    //table Injuries
    private static final String INJURIES_ROWID = "_id";
    public static final String INJURIES_TITLE = "title";
    public static final String INJURIES_DESCRIPTION = "description";
    private static final String INJURIES_PARTICIPANTID = "participantid";
    private static final String INJURIES_EVENTID = "eventid";

    private static final String[] ALL_KEYS_INJURIES = new String[] {INJURIES_ROWID, INJURIES_TITLE, INJURIES_DESCRIPTION, INJURIES_PARTICIPANTID, INJURIES_EVENTID};

    //table Registr
    private static final String REGISTR_ROWID = "_id";
    private static final String REGISTR_EVENTID = "eventid";
    private static final String REGISTR_PARTICIPANTID = "participantid";

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
                    + PARTICIPANTS_SURNAME + " TEXT NOT NULL "
                    + ");";

    //SQL to create table Registr
    private static final String DATABASE_CREATE_SQL_REGISTR =
                "CREATE TABLE " + TABLE_REGISTR
                    + " (" + REGISTR_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + REGISTR_EVENTID + " TEXT NOT NULL, "
                    + REGISTR_PARTICIPANTID + " TEXT NOT NULL"
                    + ");";

    //SQL to create table Injuries
    private static final String DATABASE_CREATE_SQL_INJUERIES =
            "CREATE TABLE " + TABLE_INJURIES
                + " (" + INJURIES_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + INJURIES_TITLE + " TEXT NOT NULL, "
                + INJURIES_DESCRIPTION + " TEXT NOT NULL, "
                + INJURIES_PARTICIPANTID + " TEXT NOT NULL, "
                    + INJURIES_EVENTID + " TEXT NOT NULL"
                    + " );";

    public DatabaseHelper myDBHelper;
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

    //checks if is no Participant on this Event
    public boolean isEmptyRegistr(Long radek) {
        boolean empty = true;
        Cursor cur = db.rawQuery("SELECT COUNT(*) from "+TABLE_REGISTR+ " WHERE " + REGISTR_EVENTID+ "= " + radek, null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt (0) == 0);
        }
        cur.close();

        return empty;
    }

    public boolean isEmptyInjuries(long participant, long event) {
        boolean empty = true;
        Cursor cur = db.rawQuery("SELECT COUNT(*) from "+TABLE_INJURIES+ " WHERE " + INJURIES_EVENTID+ "= " + event + " AND " + INJURIES_PARTICIPANTID + "=" + participant, null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt (0) == 0);
        }
        cur.close();

        return empty;
    }

    //number of participants in database
    public long getParticipantsCount() {
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_PARTICIPANTS);
        db.close();
        return cnt;
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

    //returns all data from table Participants in Events
    public Cursor getAllRowsParticipantNew(long radek) {
        String S = String.valueOf(radek);
        String MY_QUERY = "SELECT p._id,p.name,p.surname FROM (" + TABLE_PARTICIPANTS + " p INNER JOIN " + TABLE_REGISTR + " r ON r." + REGISTR_PARTICIPANTID + "=p." + PARTICIPANTS_ROWID + ") INNER JOIN "
                + TABLE_EVENTS + " e ON e." + EVENTS_ROWID + "=r." + REGISTR_EVENTID + " WHERE e." + EVENTS_ROWID + "=?";
        Cursor c = db.rawQuery(MY_QUERY, new String[]{S});
        return c;
    }

    //returns all data from table Injuries in Event for Participant
    public Cursor getAllRowsInjuries(long participantid, long eventid) {
        String S = String.valueOf(participantid);
        String K = String.valueOf(eventid);
        String MY_QUERY = "SELECT * FROM " + TABLE_INJURIES + " WHERE " + INJURIES_PARTICIPANTID + "=?  AND " + INJURIES_EVENTID + "=? ";

        Cursor c = db.rawQuery(MY_QUERY, new String[]{S,K});

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

        //add dataset to table Participants
        return db.insert(TABLE_PARTICIPANTS, null, initialValues);
    }

    //creates dataset to add to the table Registr
    public long insertRowRegistr(Long idevent, Long idparticipant) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(REGISTR_EVENTID, idevent);
        initialValues.put(REGISTR_PARTICIPANTID, idparticipant);

        //add dataset to table Registr
        return db.insert(TABLE_REGISTR, null, initialValues);
    }

    //creates dataset to add to the table Injuries
    public long insertRowInjuries(String title, String description, Long idparticipant, Long idevent) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(INJURIES_TITLE, title);
        initialValues.put(INJURIES_DESCRIPTION, description);
        initialValues.put(INJURIES_PARTICIPANTID, idparticipant);
        initialValues.put(INJURIES_EVENTID, idevent);


        //add dataset to table Injuries
        return db.insert(TABLE_INJURIES, null, initialValues);
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

    //deletes row from db by REGISTR_PARTICIPANTID
    public boolean deleteRowRegistr(long rowId) {
        String where = REGISTR_PARTICIPANTID + "=" + rowId;
        return db.delete(TABLE_REGISTR, where, null) != 0;
    }

    //deletes row from db by INJURIES_PARTICIPANTID and INJURIES_EVENTID
    public boolean deleteRowInjurie(long rowId) {
        String where = INJURIES_ROWID + "=" + rowId;
        return db.delete(TABLE_INJURIES, where, null) != 0;

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
            _db.execSQL(DATABASE_CREATE_SQL_REGISTR);
            _db.execSQL(DATABASE_CREATE_SQL_INJUERIES);

        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Zničení staré db:
            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPANTS);
            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTR);
            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_INJURIES);

            // Znovuvytvoření db:
            onCreate(_db);
        }
    }
}
