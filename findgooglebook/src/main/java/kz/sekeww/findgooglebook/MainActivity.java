package kz.sekeww.findgooglebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button searchButton;
    private EditText bookNameEditText;
    private ListView listView;
    private String myKey="AIzaSyA1wkfdAKmG9rM3in0XElWWuhx7WvVT2v4";
    private JSONArray books;
    private String thumbnailString;
    private String description;
    private String fulltitle;
    private String selfLink;
    private int globalPosition;
    private String title;
    private String subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        bookNameEditText = (EditText) findViewById(R.id.bookNameEditText);
        searchButton = (Button) findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchClick();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                onListViewItemClick(position);
            }


        });

    }

    private void onListViewItemClick(final int position) {

        globalPosition = position;

        Log.d(TAG,"selflink is " + selfLink);
        Log.d(TAG,"description is " + description);

        Intent intent = new Intent(MainActivity.this, CollapsingActivity.class);

        intent.putExtra("description", description).putExtra("fulltitle",title+subtitle+"").putExtra("thumbnailString",thumbnailString);
        startActivity(intent);
    }

    private void onSearchClick() {
        //https://www.googleapis.com/books/v1/volumes?q={search_terms}

        String bookName = bookNameEditText.getText().toString();
        String url="https://www.googleapis.com/books/v1/volumes?q="+bookName.replace(" ","%20")+":keyes&key="+myKey;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG,response.toString());

                try {
                    books = response.getJSONArray("items");
                    JSONObject book = books.getJSONObject(globalPosition);
                    selfLink = book.getString("selfLink");
                    JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                    description = volumeInfo.getString("description");

                    title = volumeInfo.getString("title");
                    //subtitle = book.getString("subtitle");

                    displayBooks(books);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
            }
        });

        queue.add(request);
    }

    private void displayBooks(JSONArray books) {
        final BooksAdapter adapter = new BooksAdapter(this,books);
        listView.setAdapter(adapter);

    }
}
