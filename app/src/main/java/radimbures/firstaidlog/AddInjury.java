package radimbures.firstaidlog;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddInjury extends Fragment {

    protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;
    EditText title;
    EditText desc;
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
    ArrayList list;
    RecyclerView.Adapter Adapter;
    File file;
    Uri uri;
    Bitmap bitmap;



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
        camera = root.findViewById(R.id.take_photo);
        list = new ArrayList<>();
        recyclerView = root.findViewById(R.id.gallery);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Adapter = new Adapter(list);
        recyclerView.setAdapter(Adapter);
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

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            camera.setEnabled(false);
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //camera_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                file = getOutputMediaFile();
                uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".radimbures.firstaidlog.provider",getOutputMediaFile());
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(camera_intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        return root;
    }

    public void dialog() {
        final AlertDialog.Builder imageDialog = new AlertDialog.Builder(getContext());
        imageDialog.setTitle("Image");
        final View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_image, (ViewGroup) getView(), false);
        imageDialog.setView(viewInflated);
        imageDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });
        imageDialog.setNegativeButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        imageDialog.show();
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
                } else {
                    myDB.insertRowInjuries(title.getText().toString(), desc.getText().toString(),idparticipant,idevent);
                    //TODO tady pak uložím obrázky
                }
                myDB.close();
                fm.popBackStackImmediate();
                return true;
            default:

        return super.onOptionsItemSelected(item);
    }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                //bitmap = (Bitmap) data.getExtras().get("data");
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                list.add(bitmap);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                camera.setEnabled(true);
            }
        }
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "FirstAidLog");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

}
