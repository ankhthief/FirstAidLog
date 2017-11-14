package radimbures.firstaidlog;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;



/**
 * A simple {@link Fragment} subclass.
 */
public class AddInjury extends Fragment {

    EditText title;
    EditText desc;
    DBAdapter myDB;
    Long idparticipant;
    Long idevent;
    Long id;
    FragmentManager fm;
    Bundle bundle;
    Boolean novy;


    public AddInjury() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myDB =  new DBAdapter(getContext());
        fm = getFragmentManager();
        final View root = inflater.inflate(R.layout.fragment_add_injury, container, false);
        title = root.findViewById(R.id.input_injury_title);
        desc = root.findViewById(R.id.input_injury_desc);
        bundle = getArguments();
        if (bundle != null) {
            idparticipant = bundle.getLong("idparticipant");
            idevent = bundle.getLong("idevent");
            id = bundle.getLong("id");
            novy = bundle.getBoolean("novy");
            if (!novy) {
                myDB.open();
                Cursor c = myDB.db.rawQuery("SELECT * FROM injuries WHERE _id==" + id, null);
                c.moveToFirst();
                title.setText(c.getString(c.getColumnIndex("title")));
                desc.setText(c.getString(c.getColumnIndex("description")));
                c.close();
                myDB.close();
            }
        }
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_participant, menu);
        menu.findItem(R.id.action_tips).setVisible(false);
        menu.findItem(R.id.action_about).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_button:
                myDB.open();
                if (!novy) {
                    ContentValues cv = new ContentValues();
                    cv.put("title",title.getText().toString());
                    cv.put("description", desc.getText().toString());
                    myDB.db.update("injuries",cv,"_id="+id,null);
                    //myDB.db.update("injuries", cv, "participantid="+idparticipant + "AND eventid="+idevent , null);
                } else { myDB.insertRowInjuries(title.getText().toString(), desc.getText().toString(),idparticipant,idevent); }
                myDB.close();
                fm.popBackStackImmediate();
                return true;
            default:

        return super.onOptionsItemSelected(item);
    }
    }
}
