package radimbures.firstaidlog;


import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;
import com.getbase.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmptyEventsList extends Fragment {

    DBAdapter myDB;
    EditText eventName;

    public EmptyEventsList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myDB =  new DBAdapter(getContext());

        final View root = inflater.inflate(R.layout.fragment_empty_list, container, false);
        FloatingActionButton fab_add = (FloatingActionButton) root.findViewById(R.id.fab_emptyList);

        fab_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder addEventDialog = new AlertDialog.Builder(getContext());
                addEventDialog.setTitle(R.string.addEventDialog);
                final View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_event, (ViewGroup) getView(), false);
                addEventDialog.setView(viewInflated);
                eventName = (EditText) viewInflated.findViewById(R.id.add_event_name);
                addEventDialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO zde se načtou data z polí a uloží do databáze
                        String str = eventName.getText().toString();
                        myDB.open();
                        myDB.insertRowEvent(str);
                        Toast.makeText(getActivity(), R.string.event_add_toast, Toast.LENGTH_LONG).show();
                        myDB.close();
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.fragment_holder, new EventsList()).commit();


                    }
                });
                addEventDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                addEventDialog.show();
            }
        });






        return root;


    }

}
