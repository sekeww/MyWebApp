package kz.sekeww.suretgram;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton addButton;

    StickyListHeadersListView stickyListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = (FloatingActionButton) findViewById(R.id.addButton);
        stickyListView = (StickyListHeadersListView) findViewById(R.id.stickyListView);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddButtonClick();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPosts();
    }

    private void getPosts() {
        Backendless.Data.of(Post.class).find(new AsyncCallback<BackendlessCollection<Post>>() {
            @Override
            public void handleResponse(BackendlessCollection<Post> response) {
                displayPosts(response.getData());
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e("Main","failed to get posts "+fault.getMessage());
            }
        });
    }

    private void displayPosts(List<Post> data) {
        PostAdapter adapter = new PostAdapter(this, data);
        stickyListView.setAdapter(adapter);
    }


    private void onAddButtonClick() {
        Intent intent = new Intent(this, AddPostActivity.class);
        startActivity(intent);
    }
}
