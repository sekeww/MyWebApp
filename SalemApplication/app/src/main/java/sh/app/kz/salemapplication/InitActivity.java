package sh.app.kz.salemapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;

import java.io.IOException;

public class InitActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final String TAG = "";
    private ImageView profileImageView;
    private TextView nameEditText;
    private Button editImageButton;
    private Button saveButton;
    private BackendlessFile profileImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        profileImageView = (ImageView) findViewById(R.id.profileImageView);
        nameEditText = (TextView) findViewById(R.id.nameEditText);
        editImageButton = (Button) findViewById(R.id.editImageButton);
        saveButton = (Button) findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClick();

            }
        });
        editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditImageClick();
            }
        });
    }

    private void onEditImageClick() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent choosetIntent = Intent.createChooser(getIntent, "Select Image");
        choosetIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(choosetIntent, PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {

            if (resultCode == RESULT_OK) {

                Bitmap bm = null;

                if (data != null) {
                    try {
                        bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    profileImageView.setImageBitmap(bm);

                    BackendlessUser user = Backendless.UserService.CurrentUser();

                    Backendless.Files.Android.upload(bm, Bitmap.CompressFormat.JPEG, 50, user.getEmail() + "-avatar.jpeg", "avatars", new AsyncCallback<BackendlessFile>() {
                        @Override
                        public void handleResponse(BackendlessFile response) {
                            Log.d(TAG, "successfully saved file");
                            profileImageFile = response;
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.e(TAG, "failed to save file " + fault.getMessage());
                        }
                    });
                }

            }
        }
    }

    private void onSaveButtonClick() {

        BackendlessUser user = Backendless.UserService.CurrentUser();
        String name = nameEditText.getText().toString();

        user.setProperty("name", name);

        if (profileImageFile != null) {
            user.setProperty("profileImageURL", profileImageFile.getFileURL());

        }

        Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                Log.d(TAG, "successfully saved user ");
                enterApp();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "failed to save user details " + fault.getMessage());
            }
        });
    }

    private void enterApp() {
        Intent intent = new Intent(InitActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
