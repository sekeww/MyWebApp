package kz.sekeww.wordnik;


import android.widget.TextView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordFragment extends Fragment {

    private static final String ARG_WORD = "word";
    private static final String TAG = "FragmentActivity";
    private CardView cardView1;
    private CardView cardView2;



    // TODO: Rename and change types of parameters
    private String word;

    public WordFragment() {
        // Required empty public constructor
    }

    public static WordFragment newInstance(String word) {
        WordFragment fragment = new WordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_WORD, word);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            word = getArguments().getString(ARG_WORD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_word, container, false);

        TextView wordTextView = (TextView) v.findViewById(R.id.wordTextView1);


//        Button makeMainButton = (Button) v.findViewById(R.id.makeMainButton);
//
//        Button showDescriptionButton = (Button) v.findViewById(R.id.showDescriptionButton);

//        makeMainButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onMakeMainButtonClick();
//            }
//        });
        Log.d(TAG,"word is " + word);
        wordTextView.setText(word);
        return v;
    }

//    private void onMakeMainButtonClick() {
//        MainActivity main = (MainActivity) getActivity();
//        main.wordEditText.setText(word);
//        main.onFindButtonClick();
//    }

}
