package radimbures.firstaidlog;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
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
    EditText rc;
    EditText insurenceCompany;
    EditText notes;
    EditText parentEmail;
    EditText parentPhone;
    DBAdapter myDB;
    Long idparticipant;
    FragmentManager fm;
    Bundle bundle;
    Integer status;
    CheckBox checkbox_status;
    Boolean formOk;


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
        rc = root.findViewById(R.id.input_rc);
        insurenceCompany = root.findViewById(R.id.input_insurence);
        formOk = false;
        notes = root.findViewById(R.id.input_notes);
        parentEmail= root.findViewById(R.id.input_parentsemail);
        parentPhone = root.findViewById(R.id.input_parentsphone);
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

    public void validation (View root) {

        final TextInputLayout layoutName = root.findViewById(R.id.input_layout_name);
        final TextInputLayout layoutSurname = root.findViewById(R.id.input_layout_surname);
        final TextInputLayout layoutRc= root.findViewById(R.id.input_layout_rc);
        final TextInputLayout layoutInsurence = root.findViewById(R.id.input_layout_insurence);
        final TextInputLayout layoutParentsEmail = root.findViewById(R.id.input_layout_parentsemail);
        final TextInputLayout layoutParentsPhone = root.findViewById(R.id.input_layout_parentsphone);

        if (!TextUtils.isEmpty(name.getText().toString())) {
            layoutName.setErrorEnabled(false);
            formOk = true;
        } else {
            layoutName.setError("Name must be filled!");
            layoutName.setErrorEnabled(true);
            formOk = false;
        }
        if (!TextUtils.isEmpty(surname.getText().toString())) {
            layoutSurname.setErrorEnabled(false);
            formOk = true;
        } else {
            layoutSurname.setError("Surname must be filled!");
            layoutSurname.setErrorEnabled(true);
            formOk = false;
        }
        if (!TextUtils.isEmpty(rc.getText().toString())) {
            layoutRc.setErrorEnabled(false);
            formOk = true;
        } else {
            layoutRc.setError("Registration number must be filled!");
            layoutRc.setErrorEnabled(true);
            formOk = false;
        }
        if (!TextUtils.isEmpty(insurenceCompany.getText().toString())) {
            layoutInsurence.setErrorEnabled(false);
            formOk = true;
        } else {
            layoutInsurence.setError("Insurence company must be filled!");
            layoutInsurence.setErrorEnabled(true);
            formOk = false;
        }
        if (!TextUtils.isEmpty(parentEmail.getText().toString())) {
            layoutParentsEmail.setErrorEnabled(false);
            formOk = true;
        } else {
            layoutParentsEmail.setError("Parent's email must be filled!");
            layoutParentsEmail.setErrorEnabled(true);
            formOk = false;
        }
        if (!TextUtils.isEmpty(parentPhone.getText().toString())) {
            layoutParentsPhone.setErrorEnabled(false);
            formOk = true;
        } else {
            layoutParentsPhone.setError("Parent's phone number must be filled!");
            layoutParentsPhone.setErrorEnabled(true);
            formOk = false;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_button:
                validation(getView());
                if (formOk) {
                    String jmeno = name.getText().toString();
                    String prijmeni = surname.getText().toString();
                    if (checkbox_status.isChecked()) {
                        status = 1;
                    } else {
                        status = 0;
                    }
                    myDB.open();
                    if (bundle != null) {
                        ContentValues cv = new ContentValues();
                        cv.put("status", status);
                        cv.put("name", jmeno);
                        cv.put("surname", prijmeni);
                        myDB.db.update("participants", cv, "_id=" + idparticipant, null);
                    } else myDB.insertRowParticipant(status, jmeno, prijmeni);
                    myDB.close();
                    fm.popBackStackImmediate();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
