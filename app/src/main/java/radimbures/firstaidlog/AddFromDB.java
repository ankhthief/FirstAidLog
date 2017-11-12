package radimbures.firstaidlog;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFromDB extends Fragment {

    DBAdapter myDB;
    long pocet;
    String[] names;
    String[] surnames;


    public AddFromDB() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myDB =  new DBAdapter(getContext());
        final View root = inflater.inflate(R.layout.fragment_add_from_db, container, false);
        LinearLayout layout = root.findViewById(R.id.check_add_layout);
        myDB.open();
        pocet = myDB.getParticipantsCount();
        Cursor cursor = myDB.getAllRowsParticipant();
        cursor.moveToFirst();
        for(int i = 0; i < pocet; i++) {
            CheckBox cb = new CheckBox(root.getContext());
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String surname = cursor.getString(cursor.getColumnIndex("surname"));
            String text = name + " " + surname;
            cb.setText(text);
            cb.setHeight(60);
            layout.addView(cb);
            cursor.moveToNext();
        }

        myDB.close();
        return root;
    }

}
