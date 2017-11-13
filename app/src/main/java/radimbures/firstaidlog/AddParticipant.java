package radimbures.firstaidlog;


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
public class AddParticipant extends Fragment {

    EditText name;
    EditText surname;
    DBAdapter myDB;



    public AddParticipant() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myDB =  new DBAdapter(getContext());
        final View root = inflater.inflate(R.layout.fragment_add_participant, container, false);
        name = root.findViewById(R.id.input_name);
        surname = root.findViewById(R.id.input_surname);


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
                final FragmentManager fm = getFragmentManager();
                myDB.open();
                myDB.insertRowParticipant(jmeno, prijmeni);
                myDB.close();
                fm.popBackStackImmediate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
