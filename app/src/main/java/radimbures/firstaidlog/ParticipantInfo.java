package radimbures.firstaidlog;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;


/**
 * A simple {@link Fragment} subclass.
 */
public class ParticipantInfo extends Fragment {

    DBAdapter myDB;
    TextView name;
    TextView rc;
    TextView ins_company;
    TextView notes;
    TextView parents_email;
    TextView parents_phone;
    long id_eventu;
    long id_participant;
    Uri path1;
    File pdfFile;
    Button show;
    Button share;
    TextView filename;
    FloatingActionButton fab2;
    String nameString;
    String filename1;
    String path;
    Image image;
    String eventName;
    Cursor zraneni;
    Cursor fotky;
    String email;


    public ParticipantInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myDB =  new DBAdapter(getContext());
        final View root = inflater.inflate(R.layout.fragment_participant_info, container, false);
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FirstAidLog";
        fab2 = root.findViewById(R.id.fab_backup_participant);
        show = root.findViewById(R.id.show);
        notes = root.findViewById(R.id.notes);
        ins_company = root.findViewById(R.id.ins_company);
        share = root.findViewById(R.id.share);
        parents_email = root.findViewById(R.id.parents_email);
        parents_phone = root.findViewById(R.id.parents_phone);
        rc = root.findViewById(R.id.rc);
        filename = root.findViewById(R.id.filename);
        Resources res = getResources();
        Bundle bundle = getArguments();
        if (bundle != null) {
            id_eventu = bundle.getLong("idevent");
            id_participant = bundle.getLong("idparticipant");
        }

        name = root.findViewById(R.id.name);
        myDB.open();
        Cursor c = myDB.db.rawQuery("SELECT p.name, p.surname, p.personalnumber, p.insurance, p.notes, p.parentsemail, p.parentsphone FROM (participants p INNER JOIN registr r ON r.participantid=p._id) INNER JOIN events e ON e._id=r.eventid WHERE e._id==" + id_eventu+ " AND p._id=="+id_participant, null);
        c.moveToFirst();
        nameString = c.getString(c.getColumnIndex("name"))+ " " + c.getString(c.getColumnIndex("surname"));
        String s = res.getString(R.string.personal_identification_number2, c.getString(c.getColumnIndex("personalnumber")));
        String s2 = res.getString(R.string.insurence_company2, c.getString(c.getColumnIndex("insurance")));
        String s3 = res.getString(R.string.notes_optional2, c.getString(c.getColumnIndex("notes")));
        email = c.getString(c.getColumnIndex("parentsemail"));
        String s4 = res.getString(R.string.email_to_parents2, email);
        String s5 = res.getString(R.string.phone_number_to_parents2, c.getString(c.getColumnIndex("parentsphone")));
        rc.setText(s);
        ins_company.setText(s2);
        notes.setText(s3);
        parents_email.setText(s4);
        parents_phone.setText(s5);
        Cursor c1 = myDB.db.rawQuery("SELECT name FROM events WHERE _id="+id_eventu, null);
        c1.moveToFirst();
        eventName = c1.getString(c1.getColumnIndex("name"));
        filename1 = c.getString(c.getColumnIndex("name"))+ c.getString(c.getColumnIndex("surname"))+"_"+eventName+".pdf";
        c.close();
        c1.close();
        name.setText(nameString);
        boolean fileExists =  new File(path+"/"+filename1).isFile();
        if (fileExists) {
            filename.setText(filename1);
            show.setEnabled(true);
            share.setEnabled(true);
        }

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

                    intentShareFile.putExtra(Intent.EXTRA_EMAIL, email);
                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                            getString(R.string.email_sub1) + nameString + getString(R.string.email_subject2) +eventName);
                    intentShareFile.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text));

                    startActivity(Intent.createChooser(intentShareFile, getString(R.string.share_file)));
                }
            }
        });



        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
            }
        }
    }

    public void createandDisplayPdf(String text) {

        Document doc = new Document();

        try {


            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            File file = new File(dir, filename1);
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();

            Paragraph p1 = new Paragraph(getString(R.string.pdf_title)+text + ", "+eventName);
            Font paraFont= new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.NORMAL, BaseColor.BLUE);
            Font nadpis = new Font(Font.FontFamily.HELVETICA,15, Font.BOLD);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setFont(nadpis);
            DottedLineSeparator dottedline = new DottedLineSeparator();
            dottedline.setOffset(-2);
            dottedline.setGap(2f);
            p1.add(dottedline);
            p1.add(Chunk.NEWLINE);
            Paragraph p2 = new Paragraph();
            p2.setAlignment(Paragraph.ALIGN_LEFT);
            p2.setFont(paraFont);
            doc.add(p1);
            myDB.open();
            zraneni = myDB.getAllRowsIncidents(id_participant, id_eventu);
            if (zraneni != null)
                if (zraneni.moveToFirst()) {
                    do {

                        String title = zraneni.getString(zraneni.getColumnIndex("title"));
                        String description = zraneni.getString(zraneni.getColumnIndex("description"));
                        Long idcko = zraneni.getLong(zraneni.getColumnIndex("_id"));
                        Chunk c = new Chunk(title, paraFont);
                        p2.add(c);
                        p2.add(Chunk.NEWLINE);
                        Chunk d = new Chunk(description, paraFont);
                        p2.add(d);
                        p2.add(Chunk.NEWLINE);
                        fotky = myDB.getAllPhotos(idcko);
                        if (fotky != null)
                            if (fotky.moveToFirst()) {
                                do {
                                    String cesta = fotky.getString(fotky.getColumnIndex("photo"));
                                    image = Image.getInstance(cesta);
                                    image.scalePercent(2);
                                    p2.add(image);
                                } while (fotky.moveToNext());
                            }
                            p2.add(dottedline);
                        p2.add(Chunk.NEWLINE);
                    } while (zraneni.moveToNext());
                }
            p2.add(Chunk.NEWLINE);
            doc.add(p2);

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally {
            myDB.close();
            doc.close();
        }

        filename.setText(filename1);
        show.setEnabled(true);
        share.setEnabled(true);
    }

    private void viewPdf(String file, String directory) {

        pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        if (Build.VERSION.SDK_INT > 21) {
            path1 = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".radimbures.firstaidlog.provider", pdfFile);
        } else {
            path1 = Uri.fromFile(pdfFile);
        }
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path1, "application/pdf");
        pdfIntent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivity(pdfIntent);
    }

}
