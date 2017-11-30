package radimbures.firstaidlog;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutApp extends Fragment {

    Button rate;


    public AboutApp() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_about_app, container, false);

        rate = root.findViewById(R.id.rate_button);

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO přesměrovat na hodnocení Google Play
            }
        });



        return root;
    }

}
