package radimbures.firstaidlog;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class ParticipantDatabase extends Fragment {

    ListView participantsDatabase;
    DBAdapter myDB;
    TextView tv_empty;
    FloatingActionButton fab;
    FragmentManager fm;


    public ParticipantDatabase() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myDB =  new DBAdapter(getContext());
        final View root = inflater.inflate(R.layout.fragment_participant_database, container, false);
        participantsDatabase = root.findViewById(R.id.list_participants_database);
        tv_empty= root.findViewById(R.id.tv_empty);
        tv_empty.setVisibility(View.GONE);
        fm = getActivity().getSupportFragmentManager();
        fab = root.findViewById(R.id.fab_event);
        populateListView();

        participantsDatabase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //uprava usera
                AddParticipant fragment = new AddParticipant();
                Bundle bundle = new Bundle();
                bundle.putLong("idparticipant", l);
                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.fragment_holder, fragment).addToBackStack(null).commit();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //přidání usera
                fm.beginTransaction().replace(R.id.fragment_holder, new AddParticipant()).addToBackStack(null).commit();
            }
        });

        return root;
    }


    public void populateListView() {
        myDB.open();
        Cursor cursor = myDB.getAllRowsParticipant();
        String[] fromParticipantNames = new String[] {DBAdapter.PARTICIPANTS_NAME, DBAdapter.PARTICIPANTS_SURNAME};
        int[] toViewIDs = new int[] {R.id.name_of_participant, R.id.surname_of_participant};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getActivity(),R.layout.row_participant, cursor, fromParticipantNames, toViewIDs,0 );
        participantsDatabase.setAdapter(myCursorAdapter);
        participantsDatabase.setAdapter(myCursorAdapter);
        if (myDB.isEmptyParticipant()) {
            tv_empty.setVisibility(View.VISIBLE);
        } else {
            tv_empty.setVisibility(View.GONE);
        }
        myDB.close();
    }


}
