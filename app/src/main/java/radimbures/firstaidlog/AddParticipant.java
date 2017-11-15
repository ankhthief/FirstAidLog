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
import android.widget.CheckBox;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddParticipant extends Fragment {

    EditText name;
    EditText surname;
    DBAdapter myDB;
    Long idparticipant;
    FragmentManager fm;
    Bundle bundle;
    Integer status;
    CheckBox checkbox_status;


    public AddParticipant() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myDB =  new DBAdapter(getContext());
        fm = getFragmentManager();
        final View root = inflater.inflate(R.layout.fragment_add_participant, container, false);
        name = root.findViewById(R.id.input_name);
        surname = root.findViewById(R.id.input_surname);
        checkbox_status = root.findViewById(R.id.status);
        checkbox_status.setChecked(true);
        bundle = getArguments();
        if (bundle != null) {
            myDB.open();
            idparticipant = bundle.getLong("idparticipant");
            Cursor c = myDB.db.rawQuery("SELECT * FROM participants WHERE _id=="+idparticipant, null);
            c.moveToFirst();
            name.setText(c.getString(c.getColumnIndex("name")));
            surname.setText(c.getString(c.getColumnIndex("surname")));
            status = c.getInt(c.getColumnIndex("status"));
            /*
            switch (status) {
                case 1:
                    checkbox_status.setChecked(true);
                    break;
                case 0:
                    checkbox_status.setChecked(false);
                    break;
            }
            */
            if (status != 1) checkbox_status.setChecked(false);
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
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add_participant, menu);
        menu.findItem(R.id.action_tips).setVisible(false);
        menu.findItem(R.id.action_about).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_button:
                String jmeno = name.getText().toString();
                String prijmeni = surname.getText().toString();
                if (checkbox_status.isChecked()) { status = 1;}
                else {status = 0;}
                myDB.open();
                if (bundle != null) {
                    ContentValues cv = new ContentValues();
                    cv.put("status",status);
                    cv.put("name",jmeno);
                    cv.put("surname", prijmeni);
                    myDB.db.update("participants", cv, "_id="+idparticipant, null);
                } else myDB.insertRowParticipant(status, jmeno, prijmeni);
                myDB.close();
                fm.popBackStackImmediate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
