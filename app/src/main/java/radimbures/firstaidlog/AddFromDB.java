package radimbures.firstaidlog;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFromDB extends Fragment {

    DBAdapter myDB;
    long pocet;
    Button btn_ad_group;
    Long id_eventu;
    String[] names;
    String[] surnames;
    Long id_participant;


    public AddFromDB() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myDB =  new DBAdapter(getContext());
        final View root = inflater.inflate(R.layout.fragment_add_from_db, container, false);
        final LinearLayout layout = root.findViewById(R.id.check_add_layout);
        btn_ad_group = root.findViewById(R.id.btn_add_group);
        Bundle bundle = getArguments();
        if (bundle != null) {
            id_eventu = bundle.getLong("idevent");
        }
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
            cb.setHeight(90);
            cb.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            layout.addView(cb);
            cursor.moveToNext();
        }
        myDB.close();

        btn_ad_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myDB.open();
                // here I want to write into database by selected checkboxes
                // I want to ready id of checked checkbox and pass it to id_participant
                myDB.insertRowRegistr(id_eventu, id_participant);
                myDB.close();

            }
        });

        return root;
    }

}
