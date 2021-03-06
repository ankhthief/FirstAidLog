package radimbures.firstaidlog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.database.Cursor;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;



/**
 * A simple {@link Fragment} subclass.
 */
public class ParticipantsList extends Fragment {

    DBAdapter myDB;
    ListView participantList;
    long id_eventu;
    SimpleCursorAdapter myCursorAdapter;
    Cursor cursor;
    TextView tv_empty;
    String[] fromParticipantNames;
    int[] toViewIDs;



    public ParticipantsList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myDB =  new DBAdapter(getContext());
        final FragmentManager fm = getActivity().getSupportFragmentManager();
        final View root = inflater.inflate(R.layout.fragment_participants_list, container, false);
        participantList = root.findViewById(R.id.list_participants);
        tv_empty= root.findViewById(R.id.tv_empty);
        tv_empty.setVisibility(View.GONE);
        final FloatingActionButton add_group = root.findViewById(R.id.fab_add_group);
        Bundle bundle = getArguments();
        if (bundle != null) {
            id_eventu = bundle.getLong("key");
        }
        populateListViewNew();
        registerForContextMenu(participantList);

        participantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //předání id eventu a usera
                Incidents frag = new Incidents();
                Bundle bundle = new Bundle();
                bundle.putLong("idparticipant", l);
                bundle.putLong("idevent", id_eventu);
                frag.setArguments(bundle);
                fm.beginTransaction().replace(R.id.fragment_holder, frag).addToBackStack(null).commit();
            }
        });

        add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDB.open();
                if (myDB.getParticipantsCount() == 0) {
                    Toast.makeText(getActivity(), R.string.no_particpants_in_db, Toast.LENGTH_LONG).show();
                } else {

                    AddFromDB frag = new AddFromDB();
                    Bundle bundle = new Bundle();
                    bundle.putLong("idevent", id_eventu);
                    frag.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.fragment_holder, frag).addToBackStack(null).commit();
                }

            }
        });

        return root;
    }

    public void populateListViewNew() {
        myDB.open();
        participantList.invalidateViews();
        cursor = myDB.getAllRowsParticipantNew(id_eventu);
        fromParticipantNames = new String[] {DBAdapter.PARTICIPANTS_NAME, DBAdapter.PARTICIPANTS_SURNAME, DBAdapter.PARTICIPANTS_PIN};
        toViewIDs = new int[] {R.id.name_of_participant, R.id.surname_of_participant, R.id.personal_number22};
        myCursorAdapter = new SimpleCursorAdapter(getActivity(),R.layout.row_participant, cursor, fromParticipantNames, toViewIDs,0 );
        participantList.setAdapter(myCursorAdapter);
        myCursorAdapter.notifyDataSetChanged();
        if (myDB.isEmptyRegistr(id_eventu)) {
            tv_empty.setVisibility(View.VISIBLE);
        } else {
            tv_empty.setVisibility(View.GONE);
        }
        myDB.close();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.participant_popup, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final long id = info.id;
        switch(item.getItemId()) {
            case R.id.delete_participant_popup:
                myDB.open();
                myDB.deleteRowRegistr(id);
                Toast.makeText(getActivity(),"participant deleted", Toast.LENGTH_LONG).show();
                populateListViewNew();
                myDB.close();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
