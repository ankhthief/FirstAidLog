package radimbures.firstaidlog;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
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
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static com.itextpdf.text.html.HtmlTags.FONT;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventInfo extends Fragment {

    DBAdapter myDB;
    TextView name_event;
    TextView location_event;
    TextView dates_event;
    TextView leadername_event;
    TextView leaderemail_event;
    TextView leaderphone_event;
    TextView medicname_event;
    TextView medicemail_event;
    TextView medicphone_event;
    TextView filename_event;
    TextView count;
    Button share;
    Button show;
    Long id_eventu;
    Resources res;
    File pdfFile;
    Uri path1;
    String namestring;
    String path;
    String filename1;
    String s2;
    String s5;
    String email_leader;
    String email_medic;


    public EventInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        myDB =  new DBAdapter(getContext());
        final View root = inflater.inflate(R.layout.fragment_event_info, container, false);

        FloatingActionButton fab = root.findViewById(R.id.fab_backup);
        name_event = root.findViewById(R.id.name_event);
        location_event = root.findViewById(R.id.location_event);
        dates_event = root.findViewById(R.id.dates_event);
        leadername_event = root.findViewById(R.id.leadername_event);
        leaderemail_event = root.findViewById(R.id.leaderemail_event);
        leaderphone_event = root.findViewById(R.id.leaderphone_event);
        medicname_event = root.findViewById(R.id.medicname_event);
        medicemail_event = root.findViewById(R.id.medicemail_event);
        medicphone_event = root.findViewById(R.id.medicphone_event);
        filename_event = root.findViewById(R.id.filename_event);
        count = root.findViewById(R.id.partic_count__event);
        share = root.findViewById(R.id.share_event);
        show = root.findViewById(R.id.show_event);
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FirstAidLog";
        res = getResources();
        Bundle bundle = getArguments();
        if (bundle != null) {
            id_eventu = bundle.getLong("key");
        }
        eventinfo();
        boolean fileExists =  new File(path+"/"+filename1).isFile();
        if (fileExists) {
            filename_event.setText(filename1);
            show.setEnabled(true);
            share.setEnabled(true);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
                }
                createPdf(namestring);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentShareFile = new Intent(Intent.ACTION_SEND);

                pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + "FirstAidLog" + "/" + filename1);

                if(pdfFile.exists()) {
                    //TODO odeslání emailu
                    intentShareFile.setType("application/pdf");
                    intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+Uri.fromFile(pdfFile)));
                    intentShareFile.putExtra(Intent.EXTRA_EMAIL, new String[]{email_leader, email_medic});
                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                            getString(R.string.email_sub1)  + name_event);
                    intentShareFile.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text));

                    startActivity(Intent.createChooser(intentShareFile, getString(R.string.share_file)));

                }
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPdf(filename1, "FirstAidLog");
            }
        });





        return root;
    }

    void eventinfo () {
        myDB.open();
        Cursor C = myDB.db.rawQuery("SELECT * FROM events WHERE _id =" + id_eventu,null);
        C.moveToFirst();
        name_event.setText(C.getString(C.getColumnIndex("name")));
        namestring = C.getString(C.getColumnIndex("name"));
        location_event.setText(C.getString(C.getColumnIndex("location")));
        String s = C.getString(C.getColumnIndex("startdate")) + res.getString(R.string.date_separator) + C.getString(C.getColumnIndex("enddate"));
        dates_event.setText(s);
        email_leader = C.getString(C.getColumnIndex("leaderemail"));
        email_medic = C.getString(C.getColumnIndex("medicemail"));
        String s1 = res.getString(R.string.full_name_of_event_leader2, C.getString(C.getColumnIndex("leadername")));
        s2 = res.getString(R.string.email_adress_of_event_leader2, C.getString(C.getColumnIndex("leaderemail")));
        String s3 = res.getString(R.string.phone_number_of_event_leader2, C.getString(C.getColumnIndex("leaderphone")));
        String s4 = res.getString(R.string.full_name_of_a_medic2, C.getString(C.getColumnIndex("medicname")));
        s5 = res.getString(R.string.email_adress_of_medic2, C.getString(C.getColumnIndex("medicemail")));
        String s6 = res.getString(R.string.phone_number_of_medic2, C.getString(C.getColumnIndex("medicphone")));
        String s7 = res.getString(R.string.number_of_partincipants, Long.toString(myDB.getParticipantsCountEvent(id_eventu)));
        filename1 = C.getString(C.getColumnIndex("name"))+ ".pdf";
        leadername_event.setText(s1);
        leaderemail_event.setText(s2);
        leaderphone_event.setText(s3);
        medicname_event.setText(s4);
        medicemail_event.setText(s5);
        medicphone_event.setText(s6);
        count.setText(s7);
        C.close();
        myDB.close();
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
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    public void createPdf(String text) {

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
            Font paraFont= new Font(Font.FontFamily.HELVETICA, 18,Font.NORMAL, BaseColor.BLACK);
            Font nadpis = new Font(Font.FontFamily.HELVETICA,18, Font.BOLD, BaseColor.BLACK);
            Font nadpish1 = new Font(Font.FontFamily.HELVETICA,25, Font.BOLD, BaseColor.BLACK);
            Font nadpish2 = new Font(Font.FontFamily.HELVETICA,20, Font.BOLD, BaseColor.BLUE);
            Paragraph p1 = new Paragraph(getString(R.string.pdf_title)+ " " +text, nadpish1);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.add(Chunk.NEWLINE);
            DottedLineSeparator dottedline = new DottedLineSeparator();
            dottedline.setOffset(-2);
            dottedline.setGap(2f);
            p1.add(dottedline);
            p1.add(Chunk.NEWLINE);
            p1.add(Chunk.NEWLINE);
            Paragraph p2 = new Paragraph();
            p2.setAlignment(Paragraph.ALIGN_LEFT);
            p2.setFont(paraFont);
            doc.add(p1);
            myDB.open();
/*
            zraneni = myDB.getAllRowsIncidents(id_participant, id_eventu);
            if (zraneni != null)
                if (zraneni.moveToFirst()) {
                    do {

                        String title = zraneni.getString(zraneni.getColumnIndex("title"));
                        String description = zraneni.getString(zraneni.getColumnIndex("description"));
                        String popis = zraneni.getString(zraneni.getColumnIndex("medication"));
                        String datum = zraneni.getString(zraneni.getColumnIndex("date"));
                        String cas = zraneni.getString(zraneni.getColumnIndex("time"));
                        Long idcko = zraneni.getLong(zraneni.getColumnIndex("_id"));
                        Chunk c = new Chunk(title + " - ", nadpish2);
                        p2.add(c);
                        Chunk c1 = new Chunk(" ", paraFont);
                        p2.add(c1);
                        Chunk c2 = new Chunk(datum, nadpish2);
                        p2.add(c2);
                        Chunk c4 = new Chunk(" ", paraFont);
                        p2.add(c4);
                        Chunk c3 = new Chunk(cas, nadpish2);
                        p2.add(c3);
                        p2.add(Chunk.NEWLINE);
                        Chunk e2 = new Chunk(getResources().getString(R.string.incident_description) + ":", nadpis);
                        p2.add(e2);
                        p2.add(Chunk.NEWLINE);
                        Chunk d = new Chunk(description, paraFont);
                        p2.add(d);
                        p2.add(Chunk.NEWLINE);
                        Chunk e1 = new Chunk(getResources().getString(R.string.description_of_medication) + ":", nadpis);
                        p2.add(e1);
                        p2.add(Chunk.NEWLINE);
                        Chunk e = new Chunk(popis, paraFont);
                        p2.add(e);
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
                        p2.add(Chunk.NEWLINE);
                        p2.add(dottedline);
                        p2.add(Chunk.NEWLINE);
                        p2.add(Chunk.NEWLINE);
                    } while (zraneni.moveToNext());
                }*/
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


        filename_event.setText(filename1);
        show.setEnabled(true);
        share.setEnabled(true);
    }

}
