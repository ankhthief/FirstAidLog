package radimbures.firstaidlog;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
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
import android.widget.EditText;
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
    EditText injuryTitle;
    EditText injuryDesc;

    public InjuriesList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myDB =  new DBAdapter(getContext());
        final FragmentManager fm = getFragmentManager();
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
                final AlertDialog.Builder addInjuryDialog = new AlertDialog.Builder(getContext());
                addInjuryDialog.setTitle("Add new Injury");
                final View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_injury, (ViewGroup) getView(), false);
                addInjuryDialog.setView(viewInflated);
                injuryTitle = viewInflated.findViewById(R.id.add_injury_title);
                injuryDesc = viewInflated.findViewById(R.id.add_injury_desc);
                addInjuryDialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO zde se načtou data z polí a uloží do databáze
                        String title = injuryTitle.getText().toString();
                        String desc = injuryDesc.getText().toString();
                        //TODO kontrola, ze jsou zadany hodnoty
                        myDB.open();
                        myDB.insertRowInjuries(title, desc, id_participant, id_eventu);
                        Toast.makeText(getActivity(), "Injury added", Toast.LENGTH_LONG).show();
                        myDB.close();
                        populateListView();
                    }
                }) .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                addInjuryDialog.show();
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
                final android.app.AlertDialog.Builder addInjuryDialog = new android.app.AlertDialog.Builder(getContext());
                addInjuryDialog.setTitle("Edit injury");
                final View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_injury, (ViewGroup) getView(), false);
                addInjuryDialog.setView(viewInflated);
                injuryTitle = viewInflated.findViewById(R.id.add_injury_title);
                injuryDesc = viewInflated.findViewById(R.id.add_injury_desc);
                myDB.open();
                Cursor c = myDB.db.rawQuery("SELECT * FROM injuries WHERE _id==" +id, null);
                c.moveToFirst();
                final String title_injury = c.getString(c.getColumnIndex("title"));
                String desc_injury = c.getString(c.getColumnIndex("description"));
                injuryTitle.setText(title_injury);
                injuryDesc.setText(desc_injury);
                c.close();
                addInjuryDialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String str1 = injuryTitle.getText().toString();
                        String str2 = injuryDesc.getText().toString();
                        ContentValues cv = new ContentValues();
                        cv.put("title", str1);
                        cv.put("description",str2);
                        myDB.db.update("injuries",cv,"_id="+id,null);
                        Toast.makeText(getActivity(),"injury changed", Toast.LENGTH_LONG).show();
                        myDB.close();
                        populateListView();
                    }
                });
                addInjuryDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                addInjuryDialog.show();
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
