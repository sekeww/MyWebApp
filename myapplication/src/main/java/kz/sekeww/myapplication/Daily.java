 package kz.sekeww.myapplication;

 import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

 /**
 * Created by Askhat on 4/12/2016.
 */
public class Daily extends Fragment {

    private static final String ARG_zNAME = "zname";
    private static final String ARG_zDESC_TODAY = "zdescToday";

    // TODO: Rename and change types of parameters
    private String zname;
    private String zodiakDescriptionToday;

    public Daily() {
        // Required empty public constructor
    }

    public static Daily newInstance(String zname, String zodiakDescriptionToday) {

        Log.d("my_log_daily_instance","zodiak name is "+zname);

        Daily fragment = new Daily();
        Bundle args = new Bundle();
        args.putString(ARG_zNAME, zname);
        args.putString(ARG_zDESC_TODAY, zodiakDescriptionToday);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("my_log_oncreate","zodiak name is "+zname);

        if (getArguments() != null) {

            zname = getArguments().getString(ARG_zNAME);
            zodiakDescriptionToday = getArguments().getString(ARG_zDESC_TODAY);

            Log.d("my_log_oncreate","zodiak name is "+zname);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_daily, container, false);

        TextView titleTextView = (TextView) v.findViewById(R.id.textTitle);
        TextView descriptionTextView = (TextView) v.findViewById(R.id.textDesc);
        TextView dateTextView = (TextView) v.findViewById(R.id.dateTextView);

        Log.d("my_log_daily_ocview","zodiak name is "+zname);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = df.format(c.getTime());

        titleTextView.setText(zname);
        descriptionTextView.setText(zodiakDescriptionToday);
        dateTextView.setText(formattedDate);

        return v;
    }
}
