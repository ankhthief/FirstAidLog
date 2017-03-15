package radimbures.firstaidlog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;


public class DatabaseHelper extends SQLiteOpenHelper {

    //TODO doplnit všechny tabulky

    //database info
    public static final String DATABASE_NAME = "firstaidlog.db";
    public static final String TABLE_EVENTS = "events";  //database name Events
    public static final int DATABASE_VERSION = 1;  //database version. Need to increment every time DB changes


    //table Events
    public static final String EVENTS_ROWID = "id";
    public static final String EVENTS_NAME = "name";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        //TODO možná přepsat tento příkaz
        //vytovoření tabulky Events
        db.execSQL("create table " + TABLE_EVENTS +" ("+EVENTS_ROWID+" integer primary key autoincrement" + EVENTS_NAME+" text not null)" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("drop table if exists" + TABLE_EVENTS);
        onCreate(db);

    }

}
