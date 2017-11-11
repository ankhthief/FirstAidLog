package radimbures.firstaidlog;


import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.database.Cursor;
import android.widget.EditText;
import android.content.DialogInterface;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;


/**
 * A simple {@link Fragment} subclass.
 */
public class ParticipantsList extends DialogFragment {

    DBAdapter myDB;
    ListView participantList;
    long id_eventu;
    EditText participantName;
    EditText participantSurname;
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
        final View root = inflater.inflate(R.layout.fragment_participants_list, container, false);
        final FragmentManager fm = getFragmentManager();
        myDB =  new DBAdapter(getContext());
        participantList = (ListView) root.findViewById(R.id.list_participants);
        tv_empty= (TextView) root.findViewById(R.id.tv_empty);
        tv_empty.setVisibility(View.GONE);
        //FloatingActionButton fab1 = (FloatingActionButton) root.findViewById(R.id.fab_participant);
        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) root.findViewById(R.id.multiple_actions);
        final FloatingActionButton add_new = (FloatingActionButton) root.findViewById(R.id.add_new);
        final FloatingActionButton add_from_db = (FloatingActionButton) root.findViewById(R.id.add_from_db);
        Bundle bundle = getArguments();
        if (bundle != null) {
            id_eventu = bundle.getLong("key");
        }
        populateListView();
        registerForContextMenu(participantList);


        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuMultipleActions.collapse();
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
                        //TODO kontrola, ze jsou zadany hodnoty
                        myDB.open();
                        myDB.insertRowParticipant(name, surname, id_eventu);
                        Toast.makeText(getActivity(), "Participant added", Toast.LENGTH_LONG).show();
                        //TODO listview refresh
                        myDB.close();
                        populateListView();
                         /* *//*fm.beginTransaction().replace(R.id.fragment_holder, new Participants()).commit();*//*
                            Participants frag = new Participants();
                            Bundle bundle = new Bundle();
                            bundle.putLong("key", id_eventu);
                            //Toast.makeText(getActivity(),"id eventu: "+l, Toast.LENGTH_LONG).show();
                            frag.setArguments(bundle);
                            fm.beginTransaction().replace(R.id.fragment_holder, frag).addToBackStack(null).commit();*/
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                addParticipantDialog.show();
            }
        });

        add_from_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuMultipleActions.collapse();
                //TODO přidání z databáze
                Toast.makeText(getActivity(), "add part. from database", Toast.LENGTH_SHORT).show();
            }
        });

        //TODO tady
/*
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
                        //TODO kontrola, ze jsou zadany hodnoty
                        myDB.open();
                        myDB.insertRowParticipant(name, surname, id_eventu);
                        Toast.makeText(getActivity(), "Participant added", Toast.LENGTH_LONG).show();
                        //TODO listview refresh
                        myDB.close();
                        populateListView();
                         /* *//*fm.beginTransaction().replace(R.id.fragment_holder, new Participants()).commit();*//*
                            Participants frag = new Participants();
                            Bundle bundle = new Bundle();
                            bundle.putLong("key", id_eventu);
                            //Toast.makeText(getActivity(),"id eventu: "+l, Toast.LENGTH_LONG).show();
                            frag.setArguments(bundle);
                            fm.beginTransaction().replace(R.id.fragment_holder, frag).addToBackStack(null).commit();*/
        //TODO tady
        /*
 }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                addParticipantDialog.show();
            }
        });
        */ //TODO tady

        return root;
    }

    public void populateListView() {
        myDB.open();
        participantList.invalidateViews();
        cursor = myDB.getAllRowsParticipant(id_eventu);
        fromParticipantNames = new String[] {DBAdapter.PARTICIPANTS_NAME, DBAdapter.PARTICIPANTS_SURNAME};
        toViewIDs = new int[] {R.id.name_of_participant, R.id.surname_of_participant};
        myCursorAdapter = new SimpleCursorAdapter(getActivity(),R.layout.row_participant, cursor, fromParticipantNames, toViewIDs,0 );
        participantList.setAdapter(myCursorAdapter);
        myCursorAdapter.notifyDataSetChanged();
        if (myDB.isEmptyParticipants(id_eventu)) {
            // Toast.makeText(getActivity(),"if",Toast.LENGTH_LONG).show();
            tv_empty.setVisibility(View.VISIBLE);


        } else {
            //Toast.makeText(getActivity(),"else",Toast.LENGTH_LONG).show();

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
                final android.app.AlertDialog.Builder addEventDialog = new android.app.AlertDialog.Builder(getContext());
                addEventDialog.setTitle("Edit participant");
                final View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_participant, (ViewGroup) getView(), false);
                addEventDialog.setView(viewInflated);
                participantName = (EditText) viewInflated.findViewById(R.id.add_participant_name);
                participantSurname = (EditText) viewInflated.findViewById(R.id.add_participant_surname);
                myDB.open();
                Cursor c = myDB.db.rawQuery("SELECT * FROM participants WHERE _id=="+id, null);
                c.moveToFirst();
                String name_par = c.getString(c.getColumnIndex("name"));
                String surname_par = c.getString(c.getColumnIndex("surname"));
                participantName.setText(name_par); //tady se musí načíst data z db
                participantSurname.setText(surname_par);
                addEventDialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO zde se načtou data z polí a uloží do databáze
                        String str = participantName.getText().toString();
                        String str1 = participantSurname.getText().toString();
                        ContentValues cv = new ContentValues();
                        cv.put("name",str);
                        cv.put("surname",str1);
                        myDB.db.update("participants", cv, "_id="+id, null);
                        Toast.makeText(getActivity(),"participant changed", Toast.LENGTH_LONG).show();
                        //TODO listview refresh
                        myDB.close();
                        populateListView();
                         /* *//*fm.beginTransaction().replace(R.id.fragment_holder, new Participants()).commit();*//*


                            Participants frag = new Participants();
                            Bundle bundle = new Bundle();
                            bundle.putLong("key", id_eventu);
                            //Toast.makeText(getActivity(),"id eventu: "+l, Toast.LENGTH_LONG).show();
                            frag.setArguments(bundle);
                            fm.beginTransaction().replace(R.id.fragment_holder, frag).addToBackStack(null).commit();*/

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
                myDB.deleteRowParticipant(id);
                Toast.makeText(getActivity(),"participant deleted", Toast.LENGTH_LONG).show();
                populateListView();
                myDB.close();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
