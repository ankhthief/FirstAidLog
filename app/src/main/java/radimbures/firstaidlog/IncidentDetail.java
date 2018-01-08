package radimbures.firstaidlog;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */

public class IncidentDetail extends Fragment {

    Bundle bundle;
    DBAdapter myDB;
    Long idparticipant;
    Long idevent;
    Long id;
    Boolean novy;
    TextView title;
    TextView date;
    TextView time;
    TextView desc;
    TextView medication;
    CarouselView carouselView;
    Cursor c2;
    Bitmap bitmap;
    ArrayList<Bitmap> list;
    int pocet;
    private Bitmap foto;
    private int outWidth;
    private int outHeight;
    private int inWidth;
    private int inHeight;



    public IncidentDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        myDB =  new DBAdapter(getContext());
        final View root = inflater.inflate(R.layout.fragment_incident_detail, container, false);
        title = root.findViewById(R.id.det_title);
        date = root.findViewById(R.id.det_date);
        time = root.findViewById(R.id.det_time);
        desc = root.findViewById(R.id.det_desc);
        medication = root.findViewById(R.id.det_medication);
        pocet = 0;
        list = new ArrayList<>();
        bundle = getArguments();
        if (bundle != null) {
            idparticipant = bundle.getLong("idparticipant");
            idevent = bundle.getLong("idevent");
            id = bundle.getLong("id");
            novy = bundle.getBoolean("novy"); }
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
                            bitmap = BitmapFactory.decodeFile(cesta);
                            //Log.i(TAG, file.getAbsolutePath());
                            list.add(bitmap);
                            pocet++;

                    } while (c2.moveToNext());
                }
        }
        myDB.close();

        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageBitmap(list.get(position));

            }
        };

        carouselView = root.findViewById(R.id.carouselView);
        carouselView.setPageCount(pocet);

        carouselView.setImageListener(imageListener);

        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                AlertDialog.Builder imageDialog = new AlertDialog.Builder(root.getContext());
                imageDialog.setTitle(R.string.image);
                //View viewInflated = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_image, (ViewGroup) view,false);
                View viewInflated = LayoutInflater.from(root.getContext()).inflate(R.layout.dialog_image, null);
                imageDialog.setView(viewInflated);
                ImageView image = viewInflated.findViewById(R.id.fotka);
                foto = list.get(position);
                final int maxSize = 850;
                inWidth = foto.getWidth();
                inHeight = foto.getHeight();
                if(inWidth > inHeight){
                    outWidth = maxSize;
                    outHeight = (inHeight * maxSize) / inWidth;
                } else {
                    outHeight = maxSize;
                    outWidth = (inWidth * maxSize) / inHeight;
                }

                Bitmap resizedFoto = Bitmap.createScaledBitmap(
                        foto, outWidth, outHeight, false);
                image.setImageBitmap(resizedFoto);
                imageDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });
                /*
                imageDialog.setNegativeButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cesta = cesty.get(position);
                        File file = new File(cesta);
                        boolean deleted = file.delete();
                        remove(position);
                    }
                });
                */
                imageDialog.show();
            }
        });







        return root;
    }

}
