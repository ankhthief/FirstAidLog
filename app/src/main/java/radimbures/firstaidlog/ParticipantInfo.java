package radimbures.firstaidlog;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;


/**
 * A simple {@link Fragment} subclass.
 */
public class ParticipantInfo extends Fragment {

    DBAdapter myDB;
    TextView name;
    long id_eventu;
    long id_participant;
    Uri path;
    File pdfFile;
    Button show;
    Button share;
    TextView filename;
    FloatingActionButton fab2;
    String nameString;
    String filename1;


    public ParticipantInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myDB =  new DBAdapter(getContext());
        final View root = inflater.inflate(R.layout.fragment_participant_info, container, false);
        fab2 = root.findViewById(R.id.fab_backup_participant);
        show = root.findViewById(R.id.show);
        share = root.findViewById(R.id.share);
        filename = root.findViewById(R.id.filename);
        Bundle bundle = getArguments();
        if (bundle != null) {
            id_eventu = bundle.getLong("idevent");
            id_participant = bundle.getLong("idparticipant");
        }

        name = root.findViewById(R.id.name);
        myDB.open();
        Cursor c = myDB.db.rawQuery("SELECT p.name, p.surname FROM (participants p INNER JOIN registr r ON r.participantid=p._id) INNER JOIN events e ON e._id=r.eventid WHERE e._id==" + id_eventu+ " AND p._id=="+id_participant, null);
        c.moveToFirst();
        nameString = c.getString(c.getColumnIndex("name"))+ " " + c.getString(c.getColumnIndex("surname"));
        Cursor c1 = myDB.db.rawQuery("SELECT name FROM events WHERE _id="+id_eventu, null);
        c1.moveToFirst();
        filename1 = c.getString(c.getColumnIndex("name"))+ c.getString(c.getColumnIndex("surname"))+"_"+c1.getString(c1.getColumnIndex("name"))+".pdf";
        c.close();
        c1.close();
        name.setText(nameString);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPdf(filename1, "FirstAidLog");
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentShareFile = new Intent(Intent.ACTION_SEND);

                if(pdfFile.exists()) {
                    intentShareFile.setType("application/pdf");
                    intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+Uri.fromFile(pdfFile)));

                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                            "Sharing File...");
                    intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

                    startActivity(Intent.createChooser(intentShareFile, "Share File"));
                }
            }
        });



        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(),"export karty účastníka", Toast.LENGTH_LONG).show();

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(getActivity(),"problém", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
                }

                createandDisplayPdf(nameString);



            }
        });


        myDB.close();
        return root;

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //camera.setEnabled(true);
            }
        }
    }

    public void createandDisplayPdf(String text) {

        Document doc = new Document();

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FirstAidLog";

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            File file = new File(dir, filename1);
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();

            Paragraph p1 = new Paragraph(text);
            Font paraFont= new Font(Font.FontFamily.COURIER);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setFont(paraFont);

            //add paragraph to document
            doc.add(p1);

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally {
            doc.close();
        }

        filename.setText(filename1);
        show.setEnabled(true);
        share.setEnabled(true);

        //viewPdf("newFile.pdf", "FirstAidLog");
    }

    // Method for opening a pdf file
    private void viewPdf(String file, String directory) {

        pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        if (Build.VERSION.SDK_INT > 21) {
            path = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".radimbures.firstaidlog.provider", pdfFile);
        } else {
            path = Uri.fromFile(pdfFile);
        }
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivity(pdfIntent);

/*
        AlertDialog.Builder pdfDialog = new AlertDialog.Builder(getContext());
        pdfDialog.setTitle("Image");
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_pdf, null);
        pdfDialog.setView(viewInflated);

        pdfDialog.setPositiveButton("Prohlédnout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                pdfIntent.setDataAndType(path, "application/pdf");
                pdfIntent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivity(pdfIntent);

            }
        });
        pdfDialog.setNegativeButton("poslat", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intentShareFile = new Intent(Intent.ACTION_SEND);

                if(pdfFile.exists()) {
                    intentShareFile.setType("application/pdf");
                    intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+Uri.fromFile(pdfFile)));

                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                            "Sharing File...");
                    intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

                    startActivity(Intent.createChooser(intentShareFile, "Share File"));
                }
            }
        });
        pdfDialog.show();

*/



    }

}
