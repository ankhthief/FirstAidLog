package radimbures.firstaidlog;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.AlertDialog;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.ListView;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import com.getbase.floatingactionbutton.FloatingActionButton;




/**
 * A simple {@link Fragment} subclass.
 */
public class EventsList extends DialogFragment {

    DBAdapter myDB;
    EditText eventName;
    ListView eventList;

    public EventsList() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myDB =  new DBAdapter(getContext());
        final FragmentManager fm = getFragmentManager();
        final View root = inflater.inflate(R.layout.fragment_events_list, container, false);
        eventList = (ListView) root.findViewById(R.id.list_events);
        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab_event);
        populateListView();

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                fm.beginTransaction().replace(R.id.fragment_holder, new Participants()).addToBackStack(null).commit();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ukáže dialog
                //TODO vyčistit kod
                //LayoutInflater layoutInflaterAndroid = LayoutInflater.from(root);
                //View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);

                ///fm.beginTransaction().replace(R.id.fragment_holder, new ParticipantsList()).addToBackStack(null).commit()
                final AlertDialog.Builder addEventDialog = new AlertDialog.Builder(getContext());
                addEventDialog.setTitle(R.string.addEventDialog);
                final View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_event, (ViewGroup) getView(), false);
                addEventDialog.setView(viewInflated);
                eventName = (EditText) viewInflated.findViewById(R.id.add_event_name);
                addEventDialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO zde se načtou data z polí a uloží do databáze
                        String str = eventName.getText().toString();
                        myDB.open();
                        myDB.insertRowEvent(str);
                        Toast.makeText(getActivity(),R.string.event_add_toast, Toast.LENGTH_LONG).show();
                        myDB.close();
                        fm.beginTransaction().replace(R.id.fragment_holder, new EventsList()).commit();
                    }
                });
                addEventDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                addEventDialog.show();

            }
        });

        return root;

    }

    public void populateListView() {
        myDB.open();
        Cursor cursor = myDB.getAllRowsEvent();
        String[] fromEventNames = new String[] {DBAdapter.EVENTS_NAME};
        int[] toViewIDs = new int[] {R.id.name_of_event};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getActivity(),R.layout.row_event, cursor, fromEventNames, toViewIDs,0 );
        eventList.setAdapter(myCursorAdapter);
        myDB.close();
    }

}
