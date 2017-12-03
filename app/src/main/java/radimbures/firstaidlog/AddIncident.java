package radimbures.firstaidlog;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddIncident extends Fragment {

    protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;
    EditText title;
    EditText desc;
    EditText date;
    EditText time;
    EditText medication;
    DBAdapter myDB;
    Long idparticipant;
    Long idevent;
    Long id;
    FragmentManager fm;
    Bundle bundle;
    Boolean novy;
    Button camera;
    LinearLayout layout;
    RecyclerView recyclerView;
    ArrayList<Bitmap> list;
    RecyclerView.Adapter Adapter;
    File file;
    ArrayList<String> path;
    Uri uri;
    Bitmap bitmap;
    Cursor c2;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener Date;
    Boolean formOk;



    public AddIncident() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myDB =  new DBAdapter(getContext());
        fm = getFragmentManager();
        final View root = inflater.inflate(R.layout.fragment_add_incident, container, false);
        title = root.findViewById(R.id.input_incident_title);
        desc = root.findViewById(R.id.input_incident_desc);
        camera = root.findViewById(R.id.take_photo);
        date = root.findViewById(R.id.input_incident_date);
        time = root.findViewById(R.id.input_incident_time);
        medication = root.findViewById(R.id.input_incident_descmedic);
        formOk = false;
        myCalendar = Calendar.getInstance();
        list = new ArrayList<>();
        path = new ArrayList<>();
        recyclerView = root.findViewById(R.id.gallery);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Adapter = new Adapter(list, path);
        recyclerView.setAdapter(Adapter);
        bundle = getArguments();
        if (bundle != null) {
            idparticipant = bundle.getLong("idparticipant");
            idevent = bundle.getLong("idevent");
            id = bundle.getLong("id");
            novy = bundle.getBoolean("novy");
            if (!novy) {
                myDB.open();
                Cursor c = myDB.db.rawQuery("SELECT * FROM incidents WHERE _id==" + id, null);
                c.moveToFirst();
                title.setText(c.getString(c.getColumnIndex("title")));
                desc.setText(c.getString(c.getColumnIndex("description")));
                date.setText(c.getString(c.getColumnIndex("date")));
                time.setText(c.getString(c.getColumnIndex("time")));
                medication.setText(c.getString(c.getColumnIndex("medication")));
                c.close();
                if (!myDB.isEmptyPhotos(id)) {
                    c2 = myDB.db.rawQuery("SELECT * FROM photos WHERE injuryid==" + id, null);
                    if (c2 != null)
                        if (c2.moveToFirst()) {
                            do {
                                String cesta = c2.getString(c2.getColumnIndex("photo"));
                                boolean fileExists =  new File(cesta).isFile();
                                if (!fileExists) {
                                    myDB.db.delete("photos","photo" + "='"+cesta+"'",null);
                                } else {
                                    bitmap = BitmapFactory.decodeFile(cesta);
                                    //Log.i(TAG, file.getAbsolutePath());
                                    list.add(bitmap);
                                    path.add(cesta);
                                }
                            } while (c2.moveToNext());
                        }
                }


                myDB.close();
            }
        }

        Date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelDate();
            }

        };

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), Date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });



        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            camera.setEnabled(false);
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = getOutputMediaFile();
                uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".radimbures.firstaidlog.provider",getOutputMediaFile());
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(camera_intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        return root;
    }


    public void validation (View root) {

        final TextInputLayout layoutTitle = root.findViewById(R.id.input_layout_title);
        final TextInputLayout layoutDate = root.findViewById(R.id.input_layout_date);
        final TextInputLayout layoutTime = root.findViewById(R.id.input_layout_time);
        final TextInputLayout layoutDesc = root.findViewById(R.id.input_layout_desc);
        final TextInputLayout layoutMedication = root.findViewById(R.id.input_layout_descmedic);


        if (!TextUtils.isEmpty(title.getText().toString())) {
            layoutTitle.setErrorEnabled(false);
            formOk = true;
        } else {
            layoutTitle.setError("Title must be filled!");
            layoutTitle.setErrorEnabled(true);
            formOk = false;
        }
        if (!TextUtils.isEmpty(date.getText().toString())) {
            layoutDate.setErrorEnabled(false);
            formOk = true;
        } else {
            layoutDate.setError("Date must be filled!");
            layoutDate.setErrorEnabled(true);
            formOk = false;
        }
        if (!TextUtils.isEmpty(time.getText().toString())) {
            layoutTime.setErrorEnabled(false);
            formOk = true;
        } else {
            layoutTime.setError("Time date must be filled!");
            layoutTime.setErrorEnabled(true);
            formOk = false;
        }
        if (!TextUtils.isEmpty(desc.getText().toString())) {
            layoutDesc.setErrorEnabled(false);
            formOk = true;
        } else {
            layoutDesc.setError("Description must be filled!");
            layoutDesc.setErrorEnabled(true);
            formOk = false;
        }
        if (!TextUtils.isEmpty(medication.getText().toString())) {
            layoutMedication.setErrorEnabled(false);
            formOk = true;
        } else {
            layoutMedication.setError("Description of medication must be filled!");
            layoutMedication.setErrorEnabled(true);
            formOk = false;
        }

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

                validation(getView());
                if (formOk) {
                    myDB.open();
                    if (!novy) {
                        ContentValues cv = new ContentValues();
                        cv.put("title", title.getText().toString());
                        cv.put("description", desc.getText().toString());
                        cv.put("date",date.getText().toString());
                        cv.put("time",time.getText().toString());
                        cv.put("medication",medication.getText().toString());
                        myDB.db.update("incidents", cv, "_id=" + id, null);
                        myDB.db.delete("photos", "injuryid" + "='" + id + "'", null);
                        for (int i = 0; i < list.size(); i++) {
                            myDB.insertRowPhotos(id, path.get(i));
                        }
                    } else {
                        Long idecko = myDB.insertRowIncidents(title.getText().toString(), desc.getText().toString(), idparticipant, idevent, date.getText().toString(),time.getText().toString(), medication.getText().toString());
                        for (int i = 0; i < list.size(); i++) {
                            myDB.insertRowPhotos(idecko, path.get(i));
                        }
                    }
                    myDB.close();
                    fm.popBackStackImmediate();
                    return true;
                }
            default:

        return super.onOptionsItemSelected(item);
    }
    }

    private void updateLabelDate() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                //bitmap = (Bitmap) data.getExtras().get("data");
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                //Log.i(TAG, file.getAbsolutePath());
                list.add(bitmap);
                path.add(file.getAbsolutePath());
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                camera.setEnabled(true);
            }
        }
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(),"FirstAidLog");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

}
