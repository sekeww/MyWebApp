package kz.sekeww.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class  Details extends AppCompatActivity {

    public static String LOG_TAG = "my_log";
    int k = 0;

    private InterstitialAd interstitial;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public int resName;
    private String zodiakName;
    private String zodiakDescriptionToday;
    private String zodiakDescriptionTomorrow;
    private String zodiakDescriptionWeek;
    private String zodiakDescriptionYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        MobileAds.initialize(getApplicationContext(),getResources().getString(R.string.ads_app_id));

        AdView mAdView = (AdView) findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().addTestDevice("27B1B3B72C8B485FEA61CFA654562346").build();
        mAdView.loadAd(adRequest);

        interstitial = new InterstitialAd(getApplicationContext());
        interstitial.setAdUnitId(getResources().getString(R.string.ads_interstitialBanner_id));


        // Begin loading your interstitial.
        requestNewInterstitial();   // если закоментировать, то утечки не будет

        interstitial.setAdListener(new AdListener() {

            public void onAdLoaded() {
                displayInterstitial();
            }

            private void displayInterstitial() {
                if (interstitial.isLoaded()) {
                    interstitial.show();
                }
            }
        });

        Intent intent = getIntent();
        //получаем строку и формируем имя ресурса
        resName = intent.getIntExtra("head", 0);

//        Toast.makeText(getApplicationContext(),
//                "Position :"+resName , Toast.LENGTH_LONG)
//                .show();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Жұлдыз Жорамалы");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            // optional
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            // optional
            @Override
            public void onPageSelected(int position) {
                if (position == 1 && k==0){
                    k = k+1;
                    requestNewInterstitial();
                }
            }

            // optional
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        String url = "http://horoscope.zadr.kz/index.php/main/get_json_horoscope";

        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(LOG_TAG,response.toString());
                if(response.length()>0){
                    try {
                        JSONObject zodiakObject = response.getJSONObject(resName);
                        zodiakName = zodiakObject.getString("name");
                        zodiakDescriptionToday = zodiakObject.getString("today");
                        zodiakDescriptionTomorrow = zodiakObject.getString("tomorrow");
                        zodiakDescriptionWeek = zodiakObject.getString("week");
                        zodiakDescriptionYear = zodiakObject.getString("year");

                        Log.d(LOG_TAG,"zodiak object is "+zodiakObject);
                        Log.d(LOG_TAG,"zodiak name is "+zodiakName);
                        setupViewPager(viewPager);
                        tabLayout.setupWithViewPager(viewPager);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d(LOG_TAG, error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        "No internet connection!!!", Toast.LENGTH_LONG)
//                        .show();
            }
        });

        queue.add(request);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Log.d("my_log_inviewPager","zodiak name is "+zodiakName);

        adapter.addFragment(new Daily().newInstance(zodiakName,zodiakDescriptionToday), "Бүгінгі");
        adapter.addFragment(new Tomorrow().newInstance(zodiakName,zodiakDescriptionTomorrow), "Ертеңгі");
        adapter.addFragment(new Weekly().newInstance(zodiakName,zodiakDescriptionWeek), "Апталық");
        adapter.addFragment(new Year().newInstance(zodiakName,zodiakDescriptionYear), "Жылдық");
        viewPager.setAdapter(adapter);

    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest1 = new AdRequest.Builder().addTestDevice("27B1B3B72C8B485FEA61CFA654562346").build();
        interstitial.loadAd(adRequest1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
