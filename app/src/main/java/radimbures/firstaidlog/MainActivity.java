package radimbures.firstaidlog;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public class MainActivity extends AppCompatActivity {


    DBAdapter myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDB =  new DBAdapter(this);
        myDB.open();

        final FragmentManager fm = getSupportFragmentManager();

        if (myDB.isEmpty()) {
            fm.beginTransaction().replace(R.id.fragment_holder, new EmptyEventsList()).commit();
        } else {
            fm.beginTransaction().replace(R.id.fragment_holder, new EventsList()).commit();
        }
        myDB.close();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}