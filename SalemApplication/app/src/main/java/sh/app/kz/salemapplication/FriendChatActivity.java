package sh.app.kz.salemapplication;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.MessageStatus;
import com.backendless.messaging.PublishOptions;
import com.bumptech.glide.Glide;

import java.util.List;

public class FriendChatActivity extends AppCompatActivity implements MessagesListener {

    private static final String TAG = "FriendChat";
    private BackendlessUser friend;

    private ImageView profileImageView;
    private TextView nameTextView;
    private EditText messageEditText;
    private Button sendButton;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_chat);

        String friendId = getIntent().getExtras().getString("friendId");
        friend = DataHolder.getDataHolder().getFriend(friendId);

        Log.d(TAG, "friend name is " + friend.getProperty("name") + ". His email is " + friend.getEmail());

        profileImageView = (ImageView) findViewById(R.id.profileImageView);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        sendButton = (Button) findViewById(R.id.sendButton);
        listView = (ListView) findViewById(R.id.listView);

        nameTextView.setText(friend.getProperty("name").toString());
        Glide.with(this).load(friend.getProperty("profileImageURL")).centerCrop().into(profileImageView);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendButtonClick();
            }
        });

        DataHolder.getDataHolder().addListener(friend.getObjectId(), this);
        displayMessages();
    }

    private void onSendButtonClick() {
        final String messageText = messageEditText.getText().toString();
        messageEditText.setText("");
        PublishOptions options = new PublishOptions();
        options.setPublisherId(Backendless.UserService.loggedInUser());

        Backendless.Messaging.publish(friend.getEmail(), messageText, options, new AsyncCallback<MessageStatus>() {
            @Override
            public void handleResponse(MessageStatus response) {
                Log.d(TAG, "message status " + response.getStatus());

                ChatMessage msg = new ChatMessage(friend.getObjectId(), messageText, (String) Backendless.UserService.CurrentUser().getProperty("profileImageURL"), ChatMessage.ChatMessageType.SENT);

                DataHolder.getDataHolder().addMessage(msg);

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "error publishing message " + fault.getMessage());
            }
        });
        DataHolder.getDataHolder().addListener(friend.getObjectId(), this);
    }

    @Override
    public void onNewMessages() {
        // TODO: display messages
        Log.d(TAG, "recieved messages in the chat activity");
        displayMessages();
    }

    private void displayMessages() {
        List<ChatMessage> messageList = DataHolder.getDataHolder().getFriendMessages(friend.getObjectId());
        ChatMessagesAdapter adapter = new ChatMessagesAdapter(this, messageList);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataHolder.getDataHolder().removeListener(friend.getObjectId(), this);
    }

}
