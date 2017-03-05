package radimbures.firstaidlog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by radim on 05.03.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "firstaidlog.db";
    public static final String TABLE_EVENTS = "events";
    public static final String EVENTS_ROWID = "id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //vytovoření tabulky Events
        db.execSQL("create table " + TABLE_EVENTS +" ("+EVENTS_ROWID+" integer primary key autoincrement)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("drop table if exists" + TABLE_EVENTS);
        onCreate(db);

    }
}
