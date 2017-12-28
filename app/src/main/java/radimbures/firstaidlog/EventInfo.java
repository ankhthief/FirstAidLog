package radimbures.firstaidlog;


import android.Manifest;
import android.content.ActivityNotFoundException;
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
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;


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
    String s1;
    String s2;
    String s3;
    String s4;
    String s5;
    String s6;
    String s7;
    String email_leader;
    String email_medic;
    String event_name_string;
    Cursor C;
    Cursor zraneni;
    Cursor fotky;
    Image image;


    public EventInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        myDB =  new DBAdapter(getContext());
        final View root = inflater.inflate(R.layout.fragment_event_info, container, false);
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
            share.setEnabled(true);
        }


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
                            getString(R.string.email_sub1) + event_name_string );
                    intentShareFile.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text));

                    startActivity(Intent.createChooser(intentShareFile, getString(R.string.share_file)));

                }
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions( new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
                }
                createPdf(namestring);
                viewPdf(filename1, "FirstAidLog");
            }
        });





        return root;
    }

    void eventinfo () {
        myDB.open();
        C = myDB.db.rawQuery("SELECT * FROM events WHERE _id =" + id_eventu,null);
        C.moveToFirst();
        name_event.setText(C.getString(C.getColumnIndex("name")));
        namestring = C.getString(C.getColumnIndex("name"));
        location_event.setText(C.getString(C.getColumnIndex("location")));
        String s = C.getString(C.getColumnIndex("startdate")) + res.getString(R.string.date_separator) + C.getString(C.getColumnIndex("enddate"));
        dates_event.setText(s);
        email_leader = C.getString(C.getColumnIndex("leaderemail"));
        email_medic = C.getString(C.getColumnIndex("medicemail"));
        event_name_string = C.getString(C.getColumnIndex("name"));
        s1 = res.getString(R.string.full_name_of_event_leader2, C.getString(C.getColumnIndex("leadername")));
        s2 = res.getString(R.string.email_adress_of_event_leader2, C.getString(C.getColumnIndex("leaderemail")));
        s3 = res.getString(R.string.phone_number_of_event_leader2, C.getString(C.getColumnIndex("leaderphone")));
        s4 = res.getString(R.string.full_name_of_a_medic2, C.getString(C.getColumnIndex("medicname")));
        s5 = res.getString(R.string.email_adress_of_medic2, C.getString(C.getColumnIndex("medicemail")));
        s6 = res.getString(R.string.phone_number_of_medic2, C.getString(C.getColumnIndex("medicphone")));
        s7 = res.getString(R.string.number_of_partincipants, Long.toString(myDB.getParticipantsCountEvent(id_eventu)));
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

        try {
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path1, "application/pdf");
            pdfIntent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), R.string.no_pdf_viewer,
                    Toast.LENGTH_LONG).show();
        }

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
            BaseFont bfFreesans = BaseFont.createFont("assets/Font/FreeSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED); //diakritika funkční
            Font paraFont= new Font(bfFreesans, 18,Font.NORMAL, BaseColor.BLACK);
            Font nadpis = new Font(bfFreesans,18, Font.BOLD, BaseColor.BLACK);
            Font nadpish1 = new Font(bfFreesans,25, Font.BOLD, BaseColor.BLACK);
            Font nadpish2 = new Font(bfFreesans,20, Font.BOLD, BaseColor.BLUE);
            Font incident = new Font(bfFreesans,20, Font.BOLD, BaseColor.RED);
            Font photodesc = new Font(bfFreesans,20, Font.ITALIC, BaseColor.BLACK);
            Paragraph p1 = new Paragraph(getString(R.string.pdf_title)+ " " +text, nadpish1);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.add(Chunk.NEWLINE);
            DottedLineSeparator dottedline = new DottedLineSeparator();
            dottedline.setOffset(-2);
            dottedline.setGap(2f);
            LineSeparator ls = new LineSeparator();
            p1.add(dottedline);
            p1.add(Chunk.NEWLINE);
            p1.add(Chunk.NEWLINE);
            Paragraph p2 = new Paragraph();
            Paragraph p3 = new Paragraph();
            Paragraph p4 = new Paragraph();
            p2.setAlignment(Paragraph.ALIGN_LEFT);
            p2.setFont(paraFont);
            doc.add(p1);
            myDB.open();
            Chunk c = new Chunk(s1);
            p2.add(c);
            p2.add(Chunk.NEWLINE);
            Chunk c1 = new Chunk(s2);
            p2.add(c1);
            p2.add(Chunk.NEWLINE);
            Chunk c2 = new Chunk(s3);
            p2.add(c2);
            p2.add(Chunk.NEWLINE);
            Chunk c3 = new Chunk(s4);
            p2.add(c3);
            p2.add(Chunk.NEWLINE);
            Chunk c4 = new Chunk(s5);
            p2.add(c4);
            p2.add(Chunk.NEWLINE);
            Chunk c5 = new Chunk(s6);
            p2.add(c5);
            p2.add(Chunk.NEWLINE);
            Chunk c6 = new Chunk(s7);
            p2.add(c6);
            p2.add(Chunk.NEWLINE);
            p2.add(ls);
            p2.add(Chunk.NEWLINE);
            Chunk xy = new Chunk(getString(R.string.photos), nadpish1);
            p4.add(xy);
            p4.add(Chunk.NEWLINE);

            Cursor participant = myDB.getAllRowsParticipantNew(id_eventu);
            if (participant != null)
                if (participant.moveToFirst()) {
                    do {
                        Long idcko_part = participant.getLong(participant.getColumnIndex("_id"));
                        String jmeno = participant.getString(participant.getColumnIndex("name"));
                        String prijmeni = participant.getString(participant.getColumnIndex("surname"));
                        String rodnecislo = participant.getString(participant.getColumnIndex("personalnumber"));
                        String pojistovna = participant.getString(participant.getColumnIndex("insurance"));
                        String poznamky = participant.getString(participant.getColumnIndex("notes"));
                        String rodicemail = participant.getString(participant.getColumnIndex("parentsemail"));
                        String rodiccislo = participant.getString(participant.getColumnIndex("parentsphone"));
                        Chunk d = new Chunk(jmeno + " ", nadpish2);
                        p3.add(d);
                        Chunk d1 = new Chunk(prijmeni, nadpish2);
                        p3.add(d1);
                        p3.add(Chunk.NEWLINE);
                        Chunk d3 = new Chunk(res.getString(R.string.personal_identification_number2, rodnecislo), paraFont);
                        p3.add(d3);
                        p3.add(Chunk.NEWLINE);
                        Chunk d4 = new Chunk(res.getString(R.string.insurence_company2, pojistovna), paraFont);
                        p3.add(d4);
                        p3.add(Chunk.NEWLINE);
                        Chunk d5 = new Chunk(res.getString(R.string.notes_optional2, poznamky), paraFont);
                        p3.add(d5);
                        p3.add(Chunk.NEWLINE);
                        Chunk d6 = new Chunk(res.getString(R.string.email_to_parents2, rodicemail), paraFont);
                        p3.add(d6);
                        p3.add(Chunk.NEWLINE);
                        Chunk d7 = new Chunk(res.getString(R.string.phone_number_to_parents2, rodiccislo), paraFont);
                        p3.add(d7);
                        p3.add(Chunk.NEWLINE);
                        p3.add(Chunk.NEWLINE);
                        p3.add(dottedline);
                        p3.add(Chunk.NEWLINE);
                        p3.add(Chunk.NEWLINE);
                        zraneni = myDB.getAllRowsIncidents(idcko_part, id_eventu);
                        if (zraneni != null)
                            if (zraneni.moveToFirst()) {
                                do {

                                    String title = zraneni.getString(zraneni.getColumnIndex("title"));
                                    String description = zraneni.getString(zraneni.getColumnIndex("description"));
                                    String popis = zraneni.getString(zraneni.getColumnIndex("medication"));
                                    String datum = zraneni.getString(zraneni.getColumnIndex("date"));
                                    String cas = zraneni.getString(zraneni.getColumnIndex("time"));
                                    Long idcko = zraneni.getLong(zraneni.getColumnIndex("_id"));
                                    Chunk f = new Chunk(title + " - ", incident);
                                    p3.add(f);
                                    Chunk f1 = new Chunk(" ", incident);
                                    p3.add(f1);
                                    Chunk f2 = new Chunk(datum, incident);
                                    p3.add(f2);
                                    Chunk f4 = new Chunk(" ", incident);
                                    p3.add(f4);
                                    Chunk f3 = new Chunk(cas, incident);
                                    p3.add(f3);
                                    p3.add(Chunk.NEWLINE);
                                    Chunk e2 = new Chunk(getResources().getString(R.string.incident_description) + ":", nadpis);
                                    p3.add(e2);
                                    p3.add(Chunk.NEWLINE);
                                    Chunk q = new Chunk(description, paraFont);
                                    p3.add(q);
                                    p3.add(Chunk.NEWLINE);
                                    Chunk e1 = new Chunk(getResources().getString(R.string.description_of_medication) + ":", nadpis);
                                    p3.add(e1);
                                    p3.add(Chunk.NEWLINE);
                                    Chunk e = new Chunk(popis, paraFont);
                                    p3.add(e);
                                    p3.add(Chunk.NEWLINE);
                                    fotky = myDB.getAllPhotos(idcko);
                                    if (fotky != null)
                                        if (fotky.moveToFirst()) {
                                            do {
                                                String cesta = fotky.getString(fotky.getColumnIndex("photo"));
                                                image = Image.getInstance(cesta);
                                                image.scalePercent(16);
                                                p4.add(image);
                                                p4.add(Chunk.NEWLINE);
                                                Chunk qw = new Chunk(title, photodesc);
                                                p4.add(qw);
                                                p4.add(Chunk.NEWLINE);
                                                p4.add(Chunk.NEWLINE);
                                                p4.add(Chunk.NEWLINE);
                                            } while (fotky.moveToNext());
                                        }
                                    p3.add(Chunk.NEWLINE);
                                    p3.add(dottedline);
                                    p3.add(Chunk.NEWLINE);
                                    p3.add(Chunk.NEWLINE);
                                } while (zraneni.moveToNext());
                            }
                        p3.add(ls);
                        p3.add(Chunk.NEWLINE);
                    } while (participant.moveToNext());
        }

            p2.add(Chunk.NEWLINE);
            doc.add(p2);
            doc.add(p3);
            doc.newPage();
            doc.add(p4);
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
        share.setEnabled(true);
    }

}
