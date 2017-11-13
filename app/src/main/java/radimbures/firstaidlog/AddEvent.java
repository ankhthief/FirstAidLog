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
public class AddEvent extends Fragment {

    DBAdapter myDB;
    FragmentManager fm;
    EditText eventName;
    Bundle bundle;
    Long ideventu;


    public AddEvent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myDB =  new DBAdapter(getContext());
        fm = getFragmentManager();
        final View root = inflater.inflate(R.layout.fragment_add_event, container, false);
        eventName = root.findViewById(R.id.input_name_event);
        bundle = getArguments();
        if (bundle != null) {
            ideventu = bundle.getLong("idevent");
            myDB.open();
            Cursor c = myDB.db.rawQuery("SELECT * FROM events WHERE _id=="+ideventu, null);
            c.moveToFirst();
            eventName.setText(c.getString(c.getColumnIndex("name")));
            c.close();
            myDB.close();
        }
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
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
                if (bundle != null) {
                    ContentValues cv = new ContentValues();
                    cv.put("name", eventName.getText().toString());
                    myDB.db.update("events", cv, "_id=" + ideventu, null);
                } else  { myDB.insertRowEvent(eventName.getText().toString()); }
                myDB.close();
                fm.popBackStackImmediate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
