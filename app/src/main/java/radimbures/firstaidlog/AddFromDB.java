package radimbures.firstaidlog;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFromDB extends Fragment {

    DBAdapter myDB;
    long pocet;
    long pocet2;
    Button btn_ad_group;
    Long id_eventu;
    Long id_participant;
    Integer idcko;
    boolean existuje;



    public AddFromDB() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myDB =  new DBAdapter(getContext());
        final FragmentManager fm = getActivity().getSupportFragmentManager();
        final View root = inflater.inflate(R.layout.fragment_add_from_db, container, false);
        final LinearLayout layout = root.findViewById(R.id.check_add_layout);
        btn_ad_group = root.findViewById(R.id.btn_add_group);
        existuje = false;
        Bundle bundle = getArguments();
        if (bundle != null) {
            id_eventu = bundle.getLong("idevent");
        }
        myDB.open();
        pocet = myDB.getParticipantsCount();
        pocet2 = myDB.getRegistCount();
        Cursor cursor = myDB.getAllRowsParticipant();
        Cursor cursor2 = myDB.getAllRowsRegistr();
        cursor.moveToFirst();
        for(int i = 0; i < pocet; i++) {
            CheckBox cb = new CheckBox(root.getContext());
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String surname = cursor.getString(cursor.getColumnIndex("surname"));
            idcko =  (cursor.getInt(cursor.getColumnIndex("_id")));
            String text = name + " " + surname;
            if (cursor2 != null)
                if (cursor2.moveToFirst()) {
                    do {
                        Long event = cursor2.getLong(cursor2.getColumnIndex("eventid"));
                        Integer user = cursor2.getInt(cursor2.getColumnIndex("participantid"));
                        if (idcko.equals(user) && event.equals(id_eventu)) existuje = true;
                    } while (cursor2.moveToNext());
            }
            if (!existuje) {
            cb.setText(text);
            cb.setHeight(90);
            cb.setId(idcko);
            layout.addView(cb);
                cursor.moveToNext();
            } else cursor.moveToNext(); existuje=false;
        }

        myDB.close();

        btn_ad_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDB.open();
                int numChildren = layout.getChildCount();
                for(int i=0;i<numChildren;i++){
                    CheckBox temp;
                    temp = (CheckBox) layout.getChildAt(i);
                    if (temp.isChecked()) {
                        id_participant =  (long) temp.getId();
                        myDB.insertRowRegistr(id_eventu, id_participant);
                    }
                }
                myDB.close();
                fm.popBackStackImmediate();
            }
        });

        return root;
    }

}
