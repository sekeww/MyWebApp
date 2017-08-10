package kz.sekeww.wordnik;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "mylog";
    private EditText wordEditText;
    private Button findButton;
    private ViewPager viewPager;
    private ArrayList<String> words;
    private CirclePageIndicator titleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordEditText = (EditText) findViewById(R.id.wordEditText);
        findButton = (Button) findViewById(R.id.findButton);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        titleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);


        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFindButtonClick();
            }
        });
    }

    private void onFindButtonClick() {
        //http://api.wordnik.com:80/v4/word.json/intellection/relatedWords?useCanonical=false&relationshipTypes=synonym&limitPerRelationshipType=10&api_key=a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5
        String word = wordEditText.getText().toString();
        String url = "http://api.wordnik.com:80/v4/word.json/"+word+"/relatedWords?useCanonical=false&relationshipTypes=synonym&limitPerRelationshipType=10&api_key=a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG,response.toString());
                if(response.length()>0){
                    try {
                        JSONObject jsonObject = response.getJSONObject(0);
                        JSONArray jsonWords = jsonObject.getJSONArray("words");

                        words = new ArrayList<>();
                        for(int i=0;i<jsonWords.length();i++){
                            String word = jsonWords.getString(i);
                            words.add(word);
                        }
                        Log.d(TAG,"words are "+words);
                        displayWords();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,error.getMessage());
            }
        });

        queue.add(request);
    }

    private void displayWords() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (String word: words){
            fragments.add(WordFragment.newInstance(word));
        }
        WordsAdapter adapter = new WordsAdapter(getSupportFragmentManager(),fragments);

        viewPager.setAdapter(adapter);

        titleIndicator.setViewPager(viewPager);

    }
}
