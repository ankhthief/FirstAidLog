package radimbures.firstaidlog;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.app.AlertDialog;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.ListView;
import android.view.MenuInflater;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import com.getbase.floatingactionbutton.FloatingActionButton;
import android.content.ContentValues;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsList extends Fragment {

    DBAdapter myDB;
    EditText eventName;
    ListView eventList;
    TextView tv_empty;
    FloatingActionButton fab;

    public EventsList() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myDB =  new DBAdapter(getContext());
        final FragmentManager fm = getActivity().getSupportFragmentManager();
        final View root = inflater.inflate(R.layout.fragment_events_list, container, false);
        tv_empty= root.findViewById(R.id.tv_empty);
        tv_empty.setVisibility(View.GONE);
        eventList = root.findViewById(R.id.list_events);
        fab = root.findViewById(R.id.fab_event);
        populateListView();
        registerForContextMenu(eventList);

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //předání id eventu
                Participants frag = new Participants();
                Bundle bundle = new Bundle();
                bundle.putLong("key", l);
                frag.setArguments(bundle);
                fm.beginTransaction().replace(R.id.fragment_holder, frag).addToBackStack(null).commit();
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
                eventName = viewInflated.findViewById(R.id.add_event_name);
                addEventDialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO zde se načtou data z polí a uloží do databáze
                        String str = eventName.getText().toString();
                        myDB.open();
                        myDB.insertRowEvent(str);
                        Toast.makeText(getActivity(),R.string.event_add_toast, Toast.LENGTH_LONG).show();
                        myDB.close();
                        fm.beginTransaction().replace(R.id.fragment_holder, new FirstAidLog()).commit();
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
        if (myDB.isEmpty()) {
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
        inflater.inflate(R.menu.event_popup, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final long id = info.id;
        switch(item.getItemId()) {
            case R.id.edit_event_popup:
                final AlertDialog.Builder addEventDialog = new AlertDialog.Builder(getContext());
                addEventDialog.setTitle("Edit event");
                final View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_event, (ViewGroup) getView(), false);
                addEventDialog.setView(viewInflated);
                eventName = viewInflated.findViewById(R.id.add_event_name);
                myDB.open();
                Cursor c = myDB.db.rawQuery("SELECT * FROM events WHERE _id=="+id, null);
                c.moveToFirst();
                String s = c.getString(c.getColumnIndex("name"));
                eventName.setText(s); //tady se musí načíst data z db
                c.close();
                addEventDialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO zde se načtou data z polí a uloží do databáze
                        String str = eventName.getText().toString();

                        ContentValues cv = new ContentValues();
                        cv.put("name",str);
                        myDB.db.update("events", cv, "_id="+id, null);

                        Toast.makeText(getActivity(),"event changed", Toast.LENGTH_LONG).show();
                        myDB.close();
                        final FragmentManager fm = getFragmentManager();
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
                return true;
            case R.id.delete_event_popup:
                myDB.open();
                myDB.deleteRowEvent(id);
                Toast.makeText(getActivity(),"event deleted", Toast.LENGTH_LONG).show();
                populateListView();
                myDB.close();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
