package radimbures.firstaidlog;


import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventInfo extends Fragment {

    DBAdapter myDB;
    TextView name_event;
    TextView location_event;
    TextView dates_event;
    TextView leadername_event;
    TextView leaderemail_event;
    TextView leaderphone_event;
    TextView medicname_event;
    TextView medicemail_event;
    TextView medicphone_event;
    TextView filename_event;
    TextView count;
    Button share;
    Button show;
    Long id_eventu;
    Resources res;


    public EventInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        myDB =  new DBAdapter(getContext());
        final View root = inflater.inflate(R.layout.fragment_event_info, container, false);

        FloatingActionButton fab = root.findViewById(R.id.fab_backup);
        name_event = root.findViewById(R.id.name_event);
        location_event = root.findViewById(R.id.location_event);
        dates_event = root.findViewById(R.id.dates_event);
        leadername_event = root.findViewById(R.id.leadername_event);
        leaderemail_event = root.findViewById(R.id.leaderemail_event);
        leaderphone_event = root.findViewById(R.id.leaderphone_event);
        medicname_event = root.findViewById(R.id.medicname_event);
        medicemail_event = root.findViewById(R.id.medicemail_event);
        medicphone_event = root.findViewById(R.id.medicphone_event);
        filename_event = root.findViewById(R.id.filename_event);
        count = root.findViewById(R.id.partic_count__event);
        share = root.findViewById(R.id.share_event);
        show = root.findViewById(R.id.show_event);
        res = getResources();
        Bundle bundle = getArguments();
        if (bundle != null) {
            id_eventu = bundle.getLong("key");
        }
        eventinfo();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"export karty akce", Toast.LENGTH_LONG).show();
            }
        });





        return root;
    }

    void eventinfo () {
        myDB.open();
        Cursor C = myDB.db.rawQuery("SELECT * FROM events WHERE _id =" + id_eventu,null);
        C.moveToFirst();
        name_event.setText(C.getString(C.getColumnIndex("name")));
        location_event.setText(C.getString(C.getColumnIndex("location")));
        String s = C.getString(C.getColumnIndex("startdate")) + res.getString(R.string.date_separator) + C.getString(C.getColumnIndex("enddate"));
        dates_event.setText(s);
        String s1 = res.getString(R.string.full_name_of_event_leader2, C.getString(C.getColumnIndex("leadername")));
        String s2 = res.getString(R.string.email_adress_of_event_leader2, C.getString(C.getColumnIndex("leaderemail")));
        String s3 = res.getString(R.string.phone_number_of_event_leader2, C.getString(C.getColumnIndex("leaderphone")));
        String s4 = res.getString(R.string.full_name_of_a_medic2, C.getString(C.getColumnIndex("medicname")));
        String s5 = res.getString(R.string.email_adress_of_medic2, C.getString(C.getColumnIndex("medicemail")));
        String s6 = res.getString(R.string.phone_number_of_medic2, C.getString(C.getColumnIndex("medicphone")));
        String s7 = res.getString(R.string.number_of_partincipants, Long.toString(myDB.getParticipantsCountEvent(id_eventu)));
        leadername_event.setText(s1);
        leaderemail_event.setText(s2);
        leaderphone_event.setText(s3);
        medicname_event.setText(s4);
        medicemail_event.setText(s5);
        medicphone_event.setText(s6);
        count.setText(s7);
        C.close();
        myDB.close();
    }

}
