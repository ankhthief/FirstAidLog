package radimbures.firstaidlog;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.database.Cursor;
import android.widget.EditText;
import android.content.DialogInterface;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import static android.R.attr.defaultValue;
import static radimbures.firstaidlog.R.string.addEventDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class ParticipantsList extends DialogFragment {

    DBAdapter myDB;
    ListView participantList;
    long id_eventu;
    EditText participantName;
    EditText participantSurname;


    public ParticipantsList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_participants_list, container, false);
        myDB =  new DBAdapter(getContext());
        participantList = (ListView) root.findViewById(R.id.list_participants);
        FloatingActionButton fab1 = (FloatingActionButton) root.findViewById(R.id.fab_participant);
        Bundle bundle = getArguments();
        if (bundle != null) {
            id_eventu = bundle.getLong("key");
        }
        populateListView();

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder addParticipantDialog = new AlertDialog.Builder(getContext());
                addParticipantDialog.setTitle("Add new Participant");
                final View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_participant, (ViewGroup) getView(), false);
                addParticipantDialog.setView(viewInflated);
                participantName = (EditText) viewInflated.findViewById(R.id.add_participant_name);
                participantSurname = (EditText) viewInflated.findViewById(R.id.add_participant_surname);
                addParticipantDialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO zde se načtou data z polí a uloží do databáze
                        String name = participantName.getText().toString();
                        String surname = participantSurname.getText().toString();
                        myDB.open();
                        myDB.insertRowParticipant(name,surname, id_eventu);
                        Toast.makeText(getActivity(),"Participant added", Toast.LENGTH_LONG).show();
                        myDB.close();
                        //fm.beginTransaction().replace(R.id.fragment_holder, new EventsList()).commit();
                    }
                });
                addParticipantDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                addParticipantDialog.show();
            }
        });

        return root;
    }

    public void populateListView() {
        myDB.open();
        Cursor cursor = myDB.getAllRowsParticipant(id_eventu);
        String[] fromParticipantNames = new String[] {DBAdapter.PARTICIPANTS_NAME, DBAdapter.PARTICIPANTS_SURNAME};
        int[] toViewIDs = new int[] {R.id.name_of_participant, R.id.surname_of_participant};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getActivity(),R.layout.row_participant, cursor, fromParticipantNames, toViewIDs,0 );
        participantList.setAdapter(myCursorAdapter);
        myDB.close();
    }

}
