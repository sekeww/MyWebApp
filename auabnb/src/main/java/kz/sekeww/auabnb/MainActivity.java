package kz.sekeww.auabnb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "my_log";

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView  = (ListView) findViewById(R.id.listView);

        Backendless.initApp(this, Konst.APP_ID,Konst.ANDROID_KEY,"v1");
        downloadCities();
    }

    private void downloadCities(){



        Backendless.Persistence.of(City.class).find(new AsyncCallback<BackendlessCollection<City>>() {
            @Override
            public void handleResponse(BackendlessCollection<City> response) {
                Log.d(TAG,response.getData().toString());

                displayCities(response.getData());
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG,"some error "+ fault.getMessage());
            }
        });
    }

    private void displayCities(List<City> cities) {

        CityAdapter adapter = new CityAdapter(this, cities);
        listView.setAdapter(adapter);
    }
}
