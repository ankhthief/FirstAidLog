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
    private static final String TABLE_INCIDENTS = "incidents"; //database name Incidents
    private static final String TABLE_PHOTOS = "photos"; //table name Photos
    private static final int DATABASE_VERSION = 14;  //database version. Need to increment every time DB changes

    //table Events
    private static final String EVENTS_ROWID = "_id";
    static final String EVENTS_NAME = "name";
    static final String EVENTS_LOCATION = "location";
    static final String EVENTS_STARTDATE = "startdate";
    static final String EVENTS_ENDDATE = "enddate";
    private static final String EVENTS_LEADERNAME = "leadername";
    private static final String EVENTS_LEADEREMAIL = "leaderemail";
    private static final String EVENTS_LEADERPHONE = "leaderphone";
    private static final String EVENTS_MEDICNAME = "medicname";
    private static final String EVENTS_MEDICEMAIL = "medicemail";
    private static final String EVENTS_MEDICPHONE = "medicphone";

    private static final String[] ALL_KEYS_EVENT = new String[] {EVENTS_ROWID, EVENTS_NAME, EVENTS_LOCATION, EVENTS_STARTDATE, EVENTS_ENDDATE, EVENTS_LEADERNAME, EVENTS_LEADEREMAIL, EVENTS_LEADERPHONE, EVENTS_MEDICNAME, EVENTS_MEDICEMAIL, EVENTS_MEDICPHONE};

    //table Participants
    private static final String PARTICIPANTS_ROWID = "_id";
    private static final String PARTICIPANT_STATUS = "status";
    static final String PARTICIPANTS_NAME = "name";
    static final String PARTICIPANTS_SURNAME = "surname";
    private static final String PARTICIPANTS_PIN = "personalnumber";
    private static final String PARTICIPANTS_INSURANCE = "insurance";
    private static final String PARTICIPANTS_NOTES = "notes";
    private static final String PARTICIPANTS_PARENTSEMAIL = "parentsemail";
    private static final String PARTICIPANTS_PARENTSPHONE = "parentsphone";

    private static final String[] ALL_KEYS_PARTICIPANT = new String[]  {PARTICIPANTS_ROWID, PARTICIPANT_STATUS, PARTICIPANTS_NAME, PARTICIPANTS_SURNAME, PARTICIPANTS_PIN, PARTICIPANTS_INSURANCE, PARTICIPANTS_NOTES, PARTICIPANTS_PARENTSEMAIL, PARTICIPANTS_PARENTSPHONE};

    //table Incidents
    private static final String INCIDENTS_ROWID = "_id";
    static final String INCIDENTS_TITLE = "title";
    static final String INCIDENTS_DESCRIPTION = "description";
    private static final String INCIDENTS_PARTICIPANTID = "participantid";
    private static final String INCIDENTS_EVENTID = "eventid";
    private static final String INCIDENTS_DATE = "date";
    private static final String INCIDENTS_TIME = "time";
    private static final String INCIDENTS_MEDICATION = "medication";

    //table Registr
    private static final String REGISTR_ROWID = "_id";
    private static final String REGISTR_EVENTID = "eventid";
    private static final String REGISTR_PARTICIPANTID = "participantid";

    private static final String[] ALL_KEYS_REGISTR = new String[] {REGISTR_ROWID, REGISTR_EVENTID, REGISTR_PARTICIPANTID};

    //table Photos
    private static final String PHOTOS_ROWID = "_id";
    private static final String PHOTOS_PHOTO = "photo";
    private static final String PHOTOS_INJURYID = "injuryid";

    //SQL to create table Events
    private static final String DATABASE_CREATE_SQL_EVENTS =
            "CREATE TABLE " + TABLE_EVENTS
                    + " (" + EVENTS_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + EVENTS_NAME + " TEXT NOT NULL, "
                    + EVENTS_LOCATION + " TEXT NOT NULL, "
                    + EVENTS_STARTDATE + " TEXT NOT NULL, "
                    + EVENTS_ENDDATE + " TEXT NOT NULL, "
                    + EVENTS_LEADERNAME + " TEXT NOT NULL, "
                    + EVENTS_LEADEREMAIL + " TEXT NOT NULL, "
                    + EVENTS_LEADERPHONE + " TEXT NOT NULL, "
                    + EVENTS_MEDICNAME + " TEXT NOT NULL, "
                    + EVENTS_MEDICEMAIL + " TEXT NOT NULL, "
                    + EVENTS_MEDICPHONE + " TEXT NOT NULL "
                    + ");";

    //SQL to create table Participants
    private static final String DATABASE_CREATE_SQL_PARTICIPANTS =
            "CREATE TABLE " + TABLE_PARTICIPANTS
                    + " (" + PARTICIPANTS_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + PARTICIPANT_STATUS + " INTEGER, "
                    + PARTICIPANTS_NAME + " TEXT NOT NULL, "
                    + PARTICIPANTS_PIN + " TEXT NOT NULL, "
                    + PARTICIPANTS_INSURANCE + " TEXT NOT NULL, "
                    + PARTICIPANTS_NOTES + " TEXT, "
                    + PARTICIPANTS_SURNAME + " TEXT NOT NULL, "
                    + PARTICIPANTS_PARENTSEMAIL + " TEXT NOT NULL, "
                    + PARTICIPANTS_PARENTSPHONE + " TEXT NOT NULL "
                    + ");";

    //SQL to create table Registr
    private static final String DATABASE_CREATE_SQL_REGISTR =
                "CREATE TABLE " + TABLE_REGISTR
                    + " (" + REGISTR_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + REGISTR_EVENTID + " TEXT NOT NULL, "
                    + REGISTR_PARTICIPANTID + " TEXT NOT NULL"
                    + ");";

    //SQL to create table Incidents
    private static final String DATABASE_CREATE_SQL_INCIDENTS =
            "CREATE TABLE " + TABLE_INCIDENTS
                + " (" + INCIDENTS_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + INCIDENTS_TITLE + " TEXT NOT NULL, "
                + INCIDENTS_DESCRIPTION + " TEXT NOT NULL, "
                + INCIDENTS_PARTICIPANTID + " TEXT NOT NULL, "
                + INCIDENTS_EVENTID + " TEXT NOT NULL, "
                + INCIDENTS_DATE + " TEXT NOT NULL, "
                + INCIDENTS_TIME + " TEXT NOT NULL, "
                + INCIDENTS_MEDICATION + " TEXT NOT NULL"
                    + " );";

    //SQL to ceate table Photos
    private static final String DATABASE_CREATE_SQL_PHOTOS =
            "CREATE TABLE " + TABLE_PHOTOS
                + " (" + PHOTOS_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PHOTOS_INJURYID + " TEXT NOT NULL, "
                + PHOTOS_PHOTO + " TEXT NOT NULL"
                + " );";

    private DatabaseHelper myDBHelper;
    SQLiteDatabase db;

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
        assert cur != null;
        cur.close();

        return empty;
    }

    //checks if is no Participant on this Event
    boolean isEmptyRegistr(Long radek) {
        boolean empty = true;
        Cursor cur = db.rawQuery("SELECT COUNT(*) from "+TABLE_REGISTR+ " WHERE " + REGISTR_EVENTID+ "= " + radek, null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt (0) == 0);
        }
        assert cur != null;
        cur.close();

        return empty;
    }

    boolean isEmptyIncidents(long participant, long event) {
        boolean empty = true;
        Cursor cur = db.rawQuery("SELECT COUNT(*) from "+TABLE_INCIDENTS+ " WHERE " + INCIDENTS_EVENTID+ "= " + event + " AND " + INCIDENTS_PARTICIPANTID + "=" + participant, null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt (0) == 0);
        }
        assert cur != null;
        cur.close();

        return empty;
    }
    boolean isEmptyPhotos(long injury) {
        boolean empty = true;
        Cursor cur = db.rawQuery("SELECT COUNT(*) from "+TABLE_PHOTOS+ " WHERE " + PHOTOS_INJURYID+ "= " + injury, null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt (0) == 0);
        }
        assert cur != null;
        cur.close();

        return empty;
    }

    boolean isEmptyParticipant() {
        boolean empty = true;
        Cursor cur = db.rawQuery("SELECT COUNT(*) from "+TABLE_PARTICIPANTS, null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt (0) == 0);
        }
        assert cur != null;
        cur.close();

        return empty;
    }

    //number of participants in database
    long getParticipantsCount() {
        return DatabaseUtils.queryNumEntries(db, TABLE_PARTICIPANTS);
    }

    //number of participants in database
    long getParticipantsCountActive() {
        //return DatabaseUtils.queryNumEntries(db, TABLE_PARTICIPANTS);
        String s = "1";
        return DatabaseUtils.longForQuery(db,"SELECT COUNT(*) FROM " + TABLE_PARTICIPANTS + " WHERE " + PARTICIPANT_STATUS + "=?", new String[] {s});
    }

    long getRegistCount() {
        return DatabaseUtils.queryNumEntries(db, TABLE_REGISTR);
    }

    public long getPhotosCount() {
        return DatabaseUtils.queryNumEntries(db, TABLE_PHOTOS);
    }

    //close database
    public void close() {
        myDBHelper.close();
    }

    //returns all data from table Events
    Cursor getAllRowsEvent() {
        Cursor c = 	db.query(true, TABLE_EVENTS, ALL_KEYS_EVENT, null, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    //returns all data from table Participants
    Cursor getAllRowsParticipant() {
                Cursor c = db.query(true, TABLE_PARTICIPANTS, ALL_KEYS_PARTICIPANT, null, null, null, null, null, null);
                if (c != null) {
                        c.moveToFirst();
                    }
                return c;
            }

    Cursor getAllRowsRegistr() {
        Cursor c = db.query(true, TABLE_REGISTR, ALL_KEYS_REGISTR,null, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    //returns all data from table Participants in Events
    Cursor getAllRowsParticipantNew(long radek) {
        String S = String.valueOf(radek);
        String MY_QUERY = "SELECT p._id,p.name,p.surname,p.personalnumber,p.insurance,p.notes,p.parentsemail,p.parentsphone FROM (" + TABLE_PARTICIPANTS + " p INNER JOIN " + TABLE_REGISTR + " r ON r." + REGISTR_PARTICIPANTID + "=p." + PARTICIPANTS_ROWID + ") INNER JOIN "
                + TABLE_EVENTS + " e ON e." + EVENTS_ROWID + "=r." + REGISTR_EVENTID + " WHERE e." + EVENTS_ROWID + "=?";
        return db.rawQuery(MY_QUERY, new String[]{S});
    }

    //returns all data from table Incidents in Event for Participant
    Cursor getAllRowsIncidents(long participantid, long eventid) {
        String S = String.valueOf(participantid);
        String K = String.valueOf(eventid);
        String MY_QUERY = "SELECT * FROM " + TABLE_INCIDENTS + " WHERE " + INCIDENTS_PARTICIPANTID + "=?  AND " + INCIDENTS_EVENTID + "=? ";

        return db.rawQuery(MY_QUERY, new String[]{S,K});
    }

    Cursor getAllPhotos(long injuryid) {
        String S = String.valueOf(injuryid);
        String MY_QUERY = "SELECT * FROM " + TABLE_PHOTOS + " WHERE " + PHOTOS_INJURYID + "=?";

        return db.rawQuery(MY_QUERY, new String[]{S});
    }
    //creates dataset to add to the table Events
    long insertRowEvent(String name, String location, String startdate, String enddate, String leadername, String leaderemail, String leaderphone, String medicname, String medicemail, String medicphone) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(EVENTS_NAME, name);
        initialValues.put(EVENTS_LOCATION, location);
        initialValues.put(EVENTS_STARTDATE, startdate);
        initialValues.put(EVENTS_ENDDATE, enddate);
        initialValues.put(EVENTS_LEADERNAME, leadername);
        initialValues.put(EVENTS_LEADEREMAIL, leaderemail);
        initialValues.put(EVENTS_LEADERPHONE, leaderphone);
        initialValues.put(EVENTS_MEDICNAME, medicname);
        initialValues.put(EVENTS_MEDICEMAIL, medicemail);
        initialValues.put(EVENTS_MEDICPHONE, medicphone);

        //add dataset to table Events
        return db.insert(TABLE_EVENTS, null, initialValues);
    }

    //creates dataset to add to the table Participants
    long insertRowParticipant(Integer status, String name, String surname, String personalnumber, String insurance, String notes, String parentsemail, String parentsphone) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PARTICIPANT_STATUS, status);
        initialValues.put(PARTICIPANTS_NAME, name);
        initialValues.put(PARTICIPANTS_SURNAME, surname);
        initialValues.put(PARTICIPANTS_PIN, personalnumber);
        initialValues.put(PARTICIPANTS_INSURANCE, insurance);
        initialValues.put(PARTICIPANTS_NOTES, notes);
        initialValues.put(PARTICIPANTS_PARENTSEMAIL, parentsemail);
        initialValues.put(PARTICIPANTS_PARENTSPHONE, parentsphone);

        //add dataset to table Participants
        return db.insert(TABLE_PARTICIPANTS, null, initialValues);
    }

    //creates dataset to add to the table Registr
    long insertRowRegistr(Long idevent, Long idparticipant) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(REGISTR_EVENTID, idevent);
        initialValues.put(REGISTR_PARTICIPANTID, idparticipant);

        //add dataset to table Registr
        return db.insert(TABLE_REGISTR, null, initialValues);
    }

    //creates dataset to add to the table Incidents
    long insertRowIncidents(String title, String description, Long idparticipant, Long idevent, String date, String time, String medication) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(INCIDENTS_TITLE, title);
        initialValues.put(INCIDENTS_DESCRIPTION, description);
        initialValues.put(INCIDENTS_PARTICIPANTID, idparticipant);
        initialValues.put(INCIDENTS_EVENTID, idevent);
        initialValues.put(INCIDENTS_DATE, date);
        initialValues.put(INCIDENTS_TIME, time);
        initialValues.put(INCIDENTS_MEDICATION, medication);


        //add dataset to table Incidents
        return db.insert(TABLE_INCIDENTS, null, initialValues);
    }

    long insertRowPhotos(Long idinjury, String image) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PHOTOS_INJURYID, idinjury);
        initialValues.put(PHOTOS_PHOTO, image);

        return  db.insert(TABLE_PHOTOS, null, initialValues);
    }



    //deletes row from db by EVENT_ROWID
    boolean deleteRowEvent(long rowId) {
        String where = EVENTS_ROWID + "=" + rowId;
        return db.delete(TABLE_EVENTS, where, null) != 0;
    }

    //deletes row from db by PARTICIPANT_ROWID
    public boolean deleteRowParticipant(long rowId) {
        String where = PARTICIPANTS_ROWID + "=" + rowId;
        return db.delete(TABLE_PARTICIPANTS, where, null) != 0;
    }

    //deletes row from db by REGISTR_PARTICIPANTID
    boolean deleteRowRegistr(long rowId) {
        String where = REGISTR_PARTICIPANTID + "=" + rowId;
        return db.delete(TABLE_REGISTR, where, null) != 0;
    }

    //deletes row from db by INJURIES_PARTICIPANTID and INJURIES_EVENTID
    boolean deleteRowIncident(long rowId) {
        String where = INCIDENTS_ROWID + "=" + rowId;
        return db.delete(TABLE_INCIDENTS, where, null) != 0;

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
            _db.execSQL(DATABASE_CREATE_SQL_INCIDENTS);
            _db.execSQL(DATABASE_CREATE_SQL_PHOTOS);

        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Zničení staré db:
            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPANTS);
            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTR);
            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCIDENTS);
            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);

            // Znovuvytvoření db:
            onCreate(_db);
        }
    }
}
