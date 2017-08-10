package sh.app.kz.salemapplication;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v4.content.IntentCompat;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class RegisterActivity extends AppCompatActivity {

    private static String TAG = "REGISTER";
    private EditText emailIdText;
    private EditText passwordEditText;

    private Button registerButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Backendless.initApp(this, Konst.APP_ID, Konst.ANDROID_KEY, Konst.VERSION);

        emailIdText = (EditText) findViewById(R.id.emailIdText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        registerButton = (Button) findViewById(R.id.registerButton);
        loginButton = (Button) findViewById(R.id.loginButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegisterButtonClick();
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginButtonClick();
            }
        });

        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean response) {
                Log.d(TAG, "is user already logged in " + response);
                if (response) {
                    loginUser();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "failed to check user " + fault.getMessage());
            }
        });
    }

    private void onRegisterButtonClick() {

        String email = emailIdText.getText().toString();
        String password = passwordEditText.getText().toString();

        BackendlessUser user = new BackendlessUser();
        user.setProperty("email", email);
        user.setPassword(password);

        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                Log.d(TAG, "registered successfully ");
                onLoginButtonClick();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "failed to regiester " + fault.getMessage());
            }
        });
    }

    private void loginUser() {
        //TODO: log in user
        String currentUserId = Backendless.UserService.loggedInUser();

        Backendless.UserService.findById(currentUserId, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                Log.d(TAG, "found user successfully to save it as current user");
                Backendless.UserService.setCurrentUser(response);

                // If user has name then go to Main App, else Initialize user

                if (response.getProperty("name") == null) {
                    Intent intent = new Intent(RegisterActivity.this, InitActivity.class);
                    startActivity(intent);
                    //finish();
                } else {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    //finish();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "failed to find such user " + fault.getMessage());

            }
        });
    }

    private void onLoginButtonClick() {
        String email = emailIdText.getText().toString();
        String password = passwordEditText.getText().toString();

        Backendless.UserService.login(email, password, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                Log.d(TAG, "logged in successfully ");
                loginUser();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "failed to login " + fault.getMessage());

            }
        }, true);
    }
}
