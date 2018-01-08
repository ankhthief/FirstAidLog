package radimbures.firstaidlog;


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

import com.synnapps.carouselview.CarouselView;
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
    int[] sampleImages;
    //int[] sampleImages = {R.drawable.image1, R.drawable.image2};


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
                            int[] sampleImages = {R.drawable.image1, R.drawable.image2};
                            //Log.i(TAG, file.getAbsolutePath());
                            list.add(bitmap);
                            //path.add(cesta);

                    } while (c2.moveToNext());
                }
        }
        myDB.close();

        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                //imageView.setImageResource(sampleImages[position]);
                imageView.setImageBitmap(bitmap);
            }
        };

        carouselView = root.findViewById(R.id.carouselView);
        // tady musí přijít počet fotek z DB (COUNT)
        carouselView.setPageCount(1);

        carouselView.setImageListener(imageListener);








        return root;
    }

}
