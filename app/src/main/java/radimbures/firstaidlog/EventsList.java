package radimbures.firstaidlog;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.AlertDialog;
import android.widget.Toast;
import android.widget.EditText;




/**
 * A simple {@link Fragment} subclass.
 */
public class EventsList extends DialogFragment {

    DatabaseHelper myDB;

    public EventsList() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_events_list, container, false);
        final FragmentManager fm = getFragmentManager();
        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);
        final EditText eventName = (EditText) root.findViewById(R.id.add_event_name);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ukáže dialog
                //TODO vyčistit kod
                //LayoutInflater layoutInflaterAndroid = LayoutInflater.from(root);
                //View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);

                ///fm.beginTransaction().replace(R.id.fragment_holder, new ParticipantsList()).addToBackStack(null).commit()
                final AlertDialog.Builder addEventDialog = new AlertDialog.Builder(getContext());
                addEventDialog.setTitle(R.string.addEventDialog);
                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_event, (ViewGroup) getView(), false);
                addEventDialog.setView(viewInflated);
                addEventDialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO zde se načtou data z polí a uloží do databáze
                        String str = eventName.getText().toString();
                        boolean isInserted = myDB.insertData(str);
                        if (isInserted == true) {
                            Toast.makeText(getActivity(),"data přidána", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(getActivity(),"data nepřidána", Toast.LENGTH_LONG).show();

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
