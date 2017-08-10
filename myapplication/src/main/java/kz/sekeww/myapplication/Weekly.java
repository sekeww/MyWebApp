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
import java.util.Date;

/**
 * Created by Askhat on 4/12/2016.
 */
public class Weekly extends Fragment {

    private static final String ARG_zNAME = "zname";
    private static final String ARG_zDESC_WEEK = "zdescWeek";

    private String weekDay = "";
    private Date beginOfWeek;
    private Date endOfWeek;
    // TODO: Rename and change types of parameters
    private String zname;
    private String zodiakDescriptionWeek;

    public Weekly() {
        // Required empty public constructor
    }

    public static Weekly newInstance(String zname, String zodiakDescriptionToday) {

        Log.d("my_log_daily_instance","zodiak name is "+zname);

        Weekly fragment = new Weekly();
        Bundle args = new Bundle();
        args.putString(ARG_zNAME, zname);
        args.putString(ARG_zDESC_WEEK, zodiakDescriptionToday);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("my_log_oncreate","zodiak name is "+zname);

        if (getArguments() != null) {

            zname = getArguments().getString(ARG_zNAME);
            zodiakDescriptionWeek = getArguments().getString(ARG_zDESC_WEEK);

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
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        if (Calendar.MONDAY == dayOfWeek) {
            beginOfWeek = c.getTime();
            c.add(Calendar.DAY_OF_YEAR,6);
            endOfWeek = c.getTime();
        } else if (Calendar.TUESDAY == dayOfWeek) {
            c.add(Calendar.DAY_OF_YEAR,-1);
            beginOfWeek = c.getTime();
            c.add(Calendar.DAY_OF_YEAR,6);
            endOfWeek = c.getTime();
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            c.add(Calendar.DAY_OF_YEAR,-2);
            beginOfWeek = c.getTime();
            c.add(Calendar.DAY_OF_YEAR,6);
            endOfWeek = c.getTime();
        } else if (Calendar.THURSDAY == dayOfWeek) {
            c.add(Calendar.DAY_OF_YEAR,-3);
            beginOfWeek = c.getTime();
            c.add(Calendar.DAY_OF_YEAR,6);
            endOfWeek = c.getTime();
        } else if (Calendar.FRIDAY == dayOfWeek) {
            c.add(Calendar.DAY_OF_YEAR,-4);
            beginOfWeek = c.getTime();
            c.add(Calendar.DAY_OF_YEAR,6);
            endOfWeek = c.getTime();
        } else if (Calendar.SATURDAY == dayOfWeek) {
            c.add(Calendar.DAY_OF_YEAR,-5);
            beginOfWeek = c.getTime();
            c.add(Calendar.DAY_OF_YEAR,6);
            endOfWeek = c.getTime();
        } else if (Calendar.SUNDAY == dayOfWeek) {
            c.add(Calendar.DAY_OF_YEAR,-6);
            beginOfWeek = c.getTime();
            c.add(Calendar.DAY_OF_YEAR,6);
            endOfWeek = c.getTime();
        }

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String fdBegin = df.format(beginOfWeek);
        String fdEnd = df.format(endOfWeek);

        dateTextView.setText(fdBegin + " - " + fdEnd);
        titleTextView.setText(zname);
        descriptionTextView.setText(zodiakDescriptionWeek);
        return v;
    }
}
