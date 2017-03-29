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
    public static final int DATABASE_VERSION = 2;  //database version. Need to increment every time DB changes

    //table Events
    public static final String EVENTS_ROWID = "_id";
    public static final String EVENTS_NAME = "name";

    public static final String[] ALL_KEYS_EVENT = new String[] {EVENTS_ROWID, EVENTS_NAME};

    //SQL pro vytvoření tabulky Events
    private static final String DATABASE_CREATE_SQL_EVENTS =
            "CREATE TABLE " + TABLE_EVENTS
                    + " (" + EVENTS_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + EVENTS_NAME + " TEXT NOT NULL"
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

    public boolean isEmpty() {
        boolean empty = true;
        Cursor cur = db.rawQuery("SELECT COUNT(*) from "+TABLE_EVENTS, null);
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

    // Vrátí všechny data z tabulky Events
    public Cursor getAllRowsEvent() {
        Cursor c = 	db.query(true, TABLE_EVENTS, ALL_KEYS_EVENT, null, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Vytvoří novou sadu hodnot, která se má přidat do tabulky Events
    public long insertRowEvent(String name) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(EVENTS_NAME, name);

        // Vloží data do tabulky Events
        return db.insert(TABLE_EVENTS, null, initialValues);
    }


    // vymaže řádek z db podle EVENT_ROWID
    public boolean deleteRowEvent(long rowId) {
        String where = EVENTS_ROWID + "=" + rowId;
        return db.delete(TABLE_EVENTS, where, null) != 0;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {

            _db.execSQL(DATABASE_CREATE_SQL_EVENTS);

        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Zničení staré db:
            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);

            // Znovuvytvoření db:
            onCreate(_db);
        }
    }
}
