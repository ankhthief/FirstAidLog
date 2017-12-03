package radimbures.firstaidlog;


import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEvent extends Fragment {

    DBAdapter myDB;
    FragmentManager fm;
    EditText eventName;
    EditText eventLocation;
    EditText leaderName;
    EditText leaderEmail;
    EditText leaderPhone;
    EditText medicName;
    EditText medicEmail;
    EditText medicPhone;
    Bundle bundle;
    Long ideventu;
    Calendar myCalendarStart;
    Calendar myCalendarEnd;
    EditText startdate;
    EditText enddate;
    DatePickerDialog.OnDateSetListener StartDate;
    DatePickerDialog.OnDateSetListener EndDate;
    Boolean formOk;


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
        eventLocation = root.findViewById(R.id.input_location_event);
        startdate = root.findViewById(R.id.input_datestart_event);
        enddate = root.findViewById(R.id.input_dateend_event);
        leaderName = root.findViewById(R.id.input_nameleader_event);
        leaderEmail = root.findViewById(R.id.input_emailleader_event);
        leaderPhone = root.findViewById(R.id.input_phoneleader_event);
        medicName = root.findViewById(R.id.input_namemedic_event);
        medicEmail = root.findViewById(R.id.input_emailmedic_event);
        medicPhone = root.findViewById(R.id.input_phonemedic_event);
        myCalendarStart = Calendar.getInstance();
        myCalendarEnd = Calendar.getInstance();
        formOk = false;
        bundle = getArguments();
        if (bundle != null) {
            ideventu = bundle.getLong("idevent");
            myDB.open();
            Cursor c = myDB.db.rawQuery("SELECT * FROM events WHERE _id=="+ideventu, null);
            c.moveToFirst();
            eventName.setText(c.getString(c.getColumnIndex("name")));
            eventLocation.setText(c.getString(c.getColumnIndex("location")));
            startdate.setText(c.getString(c.getColumnIndex("startdate")));
            enddate.setText(c.getString(c.getColumnIndex("enddate")));
            leaderName.setText(c.getString(c.getColumnIndex("leadername")));
            leaderEmail.setText(c.getString(c.getColumnIndex("leaderemail")));
            leaderPhone.setText(c.getString(c.getColumnIndex("leaderphone")));
            medicName.setText(c.getString(c.getColumnIndex("medicname")));
            medicEmail.setText(c.getString(c.getColumnIndex("medicemail")));
            medicPhone.setText(c.getString(c.getColumnIndex("medicphone")));
            c.close();
            myDB.close();
        }

       StartDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();
            }

        };

        startdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), StartDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        EndDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, monthOfYear);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelEnd();
            }

        };

        enddate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), EndDate, myCalendarEnd
                        .get(Calendar.YEAR), myCalendarEnd.get(Calendar.MONTH),
                        myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    private void updateLabelStart() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startdate.setText(sdf.format(myCalendarStart.getTime()));
    }

    private void updateLabelEnd() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        enddate.setText(sdf.format(myCalendarEnd.getTime()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_participant, menu);
        menu.findItem(R.id.action_tips).setVisible(false);
        menu.findItem(R.id.action_about).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void validation (View root) {

        final TextInputLayout layoutName = root.findViewById(R.id.input_layout_name);
        final TextInputLayout layoutLocation = root.findViewById(R.id.input_layout_location);
        final TextInputLayout layoutLeaderName = root.findViewById(R.id.input_layout_nameleader);
        final TextInputLayout layoutLeaderEmail = root.findViewById(R.id.input_layout_emailleader);
        final TextInputLayout layoutLeaderPhone = root.findViewById(R.id.input_layout_phoneleader);
        final TextInputLayout layoutMedicName = root.findViewById(R.id.input_layout_namemedic);
        final TextInputLayout layoutMedicEmail = root.findViewById(R.id.input_layout_emailmedic);
        final TextInputLayout layoutMedicPhone = root.findViewById(R.id.input_layout_phonemedic);
        final TextInputLayout layoutDateStart = root.findViewById(R.id.input_layout_datestart);
        final TextInputLayout layoutDateEnd = root.findViewById(R.id.input_layout_dateend);

        if(!TextUtils.isEmpty(eventName.getText().toString())) {
            layoutName.setErrorEnabled(false);
            formOk = true;
        } else {
            layoutName.setError("Name must be filled!");
            layoutName.setErrorEnabled(true);
            formOk = false;
        }
        if(!TextUtils.isEmpty(eventLocation.getText().toString())) {
            layoutLocation.setErrorEnabled(false);
            formOk = true;
        } else {
            layoutLocation.setError("Location must be filled!");
            layoutLocation.setErrorEnabled(true);
            formOk = false;
        }
        if(!TextUtils.isEmpty(startdate.getText().toString())) {
            layoutDateStart.setErrorEnabled(false);
            formOk = true;
        } else {
            layoutDateStart.setError("Start date must be filled!");
            layoutDateStart.setErrorEnabled(true);
            formOk = false;
        }
        if(!TextUtils.isEmpty(enddate.getText().toString())) {
            layoutDateEnd.setErrorEnabled(false);
            formOk = true;
        } else {
            layoutDateEnd.setError("End date must be filled!");
            layoutDateEnd.setErrorEnabled(true);
            formOk = false;
        }
        if(!TextUtils.isEmpty(leaderName.getText().toString())) {
            layoutLeaderName.setErrorEnabled(false);
            formOk = true;
        } else {
            layoutLeaderName.setError("Leader name must be filled!");
            layoutLeaderName.setErrorEnabled(true);
            formOk = false;
        }
        if(!TextUtils.isEmpty(leaderEmail.getText().toString())) {
            layoutLeaderEmail.setErrorEnabled(false);
            formOk = true;
        } else {
            layoutLeaderEmail.setError("Leader email must be filled!");
            layoutLeaderEmail.setErrorEnabled(true);
            formOk = false;
        }
        if(!TextUtils.isEmpty(leaderPhone.getText().toString())) {
            layoutLeaderPhone.setErrorEnabled(false);
            formOk = true;
        } else {
            layoutLeaderPhone.setError("Leader phone number must be filled!");
            layoutLeaderPhone.setErrorEnabled(true);
            formOk = false;
        }
        if(!TextUtils.isEmpty(medicName.getText().toString())) {
            layoutMedicName.setErrorEnabled(false);
            formOk = true;
        } else {
            layoutMedicName.setError("Medic name must be filled!");
            layoutMedicName.setErrorEnabled(true);
            formOk = false;
        }
        if(!TextUtils.isEmpty(medicEmail.getText().toString())) {
            layoutMedicEmail.setErrorEnabled(false);
            formOk = true;
        } else {
            layoutMedicEmail.setError("Medic email must be filled!");
            layoutMedicEmail.setErrorEnabled(true);
            formOk = false;
        }
        if(!TextUtils.isEmpty(medicPhone.getText().toString())) {
            layoutMedicPhone.setErrorEnabled(false);
            formOk = true;
        } else {
            layoutMedicPhone.setError("Medic phone number must be filled!");
            layoutMedicPhone.setErrorEnabled(true);
            formOk = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_button:
                validation(getView());
                if (formOk) {
                myDB.open();
                if (bundle != null) {
                    ContentValues cv = new ContentValues();
                    cv.put("name", eventName.getText().toString());
                    cv.put("location", eventLocation.getText().toString());
                    cv.put("startdate", startdate.getText().toString());
                    cv.put("enddate", enddate.getText().toString());
                    cv.put("leadername", leaderName.getText().toString());
                    cv.put("leaderemail", leaderEmail.getText().toString());
                    cv.put("leaderphone", leaderPhone.getText().toString());
                    cv.put("medicname", medicName.getText().toString());
                    cv.put("medicemail", medicEmail.getText().toString());
                    cv.put("medicphone", medicPhone.getText().toString());
                    myDB.db.update("events", cv, "_id=" + ideventu, null);
                } else  { myDB.insertRowEvent(eventName.getText().toString(), eventLocation.getText().toString(), startdate.getText().toString(), enddate.getText().toString(), leaderName.getText().toString(), leaderEmail.getText().toString(), leaderPhone.getText().toString(), medicName.getText().toString(), medicEmail.getText().toString(), medicPhone.getText().toString()); }
                myDB.close();
                fm.popBackStackImmediate();

                return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
