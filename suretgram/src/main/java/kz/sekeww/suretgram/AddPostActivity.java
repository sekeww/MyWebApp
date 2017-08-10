package kz.sekeww.suretgram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;

import java.io.IOException;

public class AddPostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private ImageView imageView;
    private EditText messageEditText;
    private Button postButton;
    private ProgressDialog dialog;
    private BackendlessFile backendlessFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        imageView = (ImageView) findViewById(R.id.imageView);
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        postButton = (Button) findViewById(R.id.postButton);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageClick();
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPostButtonClick();
            }
        });
    }

    private void onImageClick() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/+");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/+");

        Intent chooserIntent = Intent.createChooser(getIntent(),"Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {

                Bitmap bitmap = null;
                if (data != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    imageView.setImageBitmap(bitmap);

                    dialog = new ProgressDialog(this);
                    dialog.setTitle("Uploading File");
                    dialog.setMessage("Please, wait");
                    dialog.show();

                    BackendlessUser user = Backendless.UserService.CurrentUser();
                    Backendless.Files.Android.upload(bitmap, Bitmap.CompressFormat.JPEG, 100,
                            "post" + user.getObjectId() + System.currentTimeMillis(), "posts", new AsyncCallback<BackendlessFile>() {
                                @Override
                                public void handleResponse(BackendlessFile response) {
                                    Log.d("AddPost","uploaded file");
                                    backendlessFile = response;
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Log.e("AddPost","error uploading file"+ fault.getMessage());
                                }
                            });
                }
            }
        }
    }

    private void onPostButtonClick() {
        BackendlessUser user = Backendless.UserService.CurrentUser();
        String message = messageEditText.getText().toString();

        Post post = new Post(user, backendlessFile.getFileURL(),message);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Uploading Post");
        dialog.setMessage("Please, wait");
        dialog.show();

        Backendless.Persistence.of(Post.class).save(post, new AsyncCallback<Post>() {
            @Override
            public void handleResponse(Post response) {
                if(dialog.isShowing()) {
                    dialog.dismiss();
                }
                finish();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e("AddPost","failed to save post "+fault.getMessage());
            }
        });

    }
}
