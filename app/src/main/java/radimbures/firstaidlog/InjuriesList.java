package radimbures.firstaidlog;



import android.database.Cursor;
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
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class InjuriesList extends Fragment {

    DBAdapter myDB;
    ListView injuriesList;
    TextView tv_empty;
    long id_eventu;
    long id_participant;
    Cursor cursor;
    int[] toViewIDs;
    SimpleCursorAdapter myCursorAdapter;
    String[] fromInjuriesNames;
    FloatingActionButton fab;
    FragmentManager fm;

    public InjuriesList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myDB =  new DBAdapter(getContext());
        fm  = getActivity().getSupportFragmentManager();
        final View root = inflater.inflate(R.layout.fragment_injuries_list, container, false);
        // Inflate the layout for this fragment
        fab = root.findViewById(R.id.fab_injury);
        injuriesList = root.findViewById(R.id.list_injuries);
        tv_empty= root.findViewById(R.id.tv_empty);
        tv_empty.setVisibility(View.GONE);
        Bundle bundle = getArguments();
        if (bundle != null) {
            id_eventu = bundle.getLong("idevent");
            id_participant = bundle.getLong("idparticipant");
        }
        populateListView();
        registerForContextMenu(injuriesList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddInjury frag = new AddInjury();
                Bundle bundle = new Bundle();
                bundle.putLong("idparticipant", id_participant);
                bundle.putLong("idevent", id_eventu);
                bundle.putBoolean("novy", true);
                frag.setArguments(bundle);
                fm.beginTransaction().replace(R.id.fragment_holder, frag).addToBackStack(null).commit();
            }
        });


        return root;
    }


    public void populateListView() {
        myDB.open();
        injuriesList.invalidateViews();
        cursor = myDB.getAllRowsInjuries(id_participant, id_eventu);
        fromInjuriesNames = new String[] {DBAdapter.INJURIES_TITLE, DBAdapter.INJURIES_DESCRIPTION};
        toViewIDs = new int[] {R.id.title_of_injury, R.id.desc_of_injury};
        myCursorAdapter = new SimpleCursorAdapter(getActivity(),R.layout.row_injurie, cursor, fromInjuriesNames, toViewIDs,0 );
        injuriesList.setAdapter(myCursorAdapter);
        myCursorAdapter.notifyDataSetChanged();
        if (myDB.isEmptyInjuries(id_participant, id_eventu)) {
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
        inflater.inflate(R.menu.injury_popup, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final long id = info.id;
        switch (item.getItemId()) {
            case R.id.edit_injury_popup:
                AddInjury frag = new AddInjury();
                Bundle bundle = new Bundle();
                bundle.putLong("idparticipant", id_eventu);
                bundle.putLong("idevent", id_participant);
                bundle.putLong("id", id);
                bundle.putBoolean("novy", false);
                frag.setArguments(bundle);
                fm.beginTransaction().replace(R.id.fragment_holder, frag).addToBackStack(null).commit();
                return true;
            case R.id.delete_injury_popup:
                myDB.open();
                myDB.deleteRowInjurie(id);
                Toast.makeText(getActivity(),"injury deleted", Toast.LENGTH_LONG).show();
                populateListView();
                myDB.close();
                return true;
            default:
                return super.onContextItemSelected(item);

        }
    }
}
