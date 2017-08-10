package kz.sekeww.jsonparsing;


        import android.app.ListActivity;
        import android.app.ProgressDialog;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
        import android.widget.ListAdapter;
        import android.widget.SimpleAdapter;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;

public class MainActivity extends ListActivity {

    // URL to get contacts JSON
    private static String url = "https://raw.githubusercontent.com/BilCode/AndroidJSONParsing/master/index.html";

    // JSON Node names
    private static final String TAG_STUDENT_INFO = "studentsinfo";
    private static final String TAG_ID = "id";
    private static final String TAG_STUDENT_NAME = "sname";
    private static final String TAG_EMAIL = "semail";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_STUDENT_GENDER = "gender";
    private static final String TAG_STUDENT_PHONE = "sphone";
    private static final String TAG_STUDENT_PHONE_MOBILE = "mobile";
    private static final String TAG_STUDENT_PHONE_HOME = "home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// Calling async task to get json
        new GetStudents().execute();
    }
    /**
     * Async task class to get json by making HTTP call
     */
    private class GetStudents extends AsyncTask<Void, Void, Void> {

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> studentList;
        ProgressDialog proDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
// Showing progress loading dialog
            proDialog = new ProgressDialog(MainActivity.this);
            proDialog.setMessage("Please wait...");
            proDialog.setCancelable(false);
            proDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
// Creating service handler class instance
            WebRequest webreq = new WebRequest();

// Making a request to url and getting response
            String jsonStr = webreq.makeWebServiceCall(url, WebRequest.GETRequest);

            Log.d("Response: ", "> " + jsonStr);

            studentList = ParseJSON(jsonStr);

            return null;
        }
        @Override
        protected void onPostExecute(Void requestresult) {
            super.onPostExecute(requestresult);
// Dismiss the progress dialog
            if (proDialog.isShowing())
                proDialog.dismiss();
/**
 * Updating received data from JSON into ListView
 * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, studentList,
                    R.layout.list_item, new String[]{TAG_STUDENT_NAME, TAG_EMAIL,
                    TAG_STUDENT_PHONE_MOBILE}, new int[]{R.id.name,
                    R.id.email, R.id.mobile});

            setListAdapter(adapter);
        }

    }
    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
// Hashmap for ListView
                ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();

                JSONObject jsonObj = new JSONObject(json);

// Getting JSON Array node
                JSONArray students = jsonObj.getJSONArray(TAG_STUDENT_INFO);

// looping through All Students
                for (int i = 0; i < students.length(); i++) {
                    JSONObject c = students.getJSONObject(i);

                    String id = c.getString(TAG_ID);
                    String name = c.getString(TAG_STUDENT_NAME);
                    String email = c.getString(TAG_EMAIL);
                    String address = c.getString(TAG_ADDRESS);
                    String gender = c.getString(TAG_STUDENT_GENDER);

// Phone node is JSON Object
                    JSONObject phone = c.getJSONObject(TAG_STUDENT_PHONE);
                    String mobile = phone.getString(TAG_STUDENT_PHONE_MOBILE);
                    String home = phone.getString(TAG_STUDENT_PHONE_HOME);

// tmp hashmap for single student
                    HashMap<String, String> student = new HashMap<String, String>();

// adding every child node to HashMap key => value
                    student.put(TAG_ID, id);
                    student.put(TAG_STUDENT_NAME, name);
                    student.put(TAG_EMAIL, email);
                    student.put(TAG_STUDENT_PHONE_MOBILE, mobile);

// adding student to students list
                    studentList.add(student);
                }
                return studentList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "No data received from HTTP Request");
            return null;
        }
    }
}