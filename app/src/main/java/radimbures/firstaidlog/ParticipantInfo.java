package radimbures.firstaidlog;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class ParticipantInfo extends Fragment {

    DBAdapter myDB;
    TextView name;
    long id_eventu;
    long id_participant;


    public ParticipantInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myDB =  new DBAdapter(getContext());
        final View root = inflater.inflate(R.layout.fragment_participant_info, container, false);
        FloatingActionButton fab2 = root.findViewById(R.id.fab_backup_participant);
        Bundle bundle = getArguments();
        if (bundle != null) {
            id_eventu = bundle.getLong("idevent");
            id_participant = bundle.getLong("idparticipant");
        }

        name = root.findViewById(R.id.name);
        myDB.open();
        Cursor c = myDB.db.rawQuery("SELECT p.name, p.surname FROM (participants p INNER JOIN registr r ON r.participantid=p._id) INNER JOIN events e ON e._id=r.eventid WHERE e._id==" + id_eventu+ " AND p._id=="+id_participant, null);
        c.moveToFirst();
        String nameString = c.getString(c.getColumnIndex("name"))+ " " + c.getString(c.getColumnIndex("surname"));
        c.close();
        name.setText(nameString);

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"export karty účastníka", Toast.LENGTH_LONG).show();
            }
        });


        myDB.close();
        return root;

    }

}
