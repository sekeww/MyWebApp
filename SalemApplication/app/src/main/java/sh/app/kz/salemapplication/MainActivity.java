package sh.app.kz.salemapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.Subscription;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.Message;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button addFriendButton;
    private ListView listView;
    private ArrayList<BackendlessUser> friends;
    private ArrayList<String> fatFriends;
    private boolean notEqual = false;
    private Button logout;
    private FriendsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        addFriendButton = (Button) findViewById(R.id.addFriendButton);
        logout = (Button) findViewById(R.id.logoutButton);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogoutClick();
            }
        });

        fatFriends = new ArrayList<String>();

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddFriendClick();
            }
        });

        getFriends();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onFriendClick(position);
            }
        });

        //Subscribe for incoming messages

        Backendless.Messaging.subscribe(Backendless.UserService.CurrentUser().getEmail(), new AsyncCallback<List<Message>>() {
            @Override
            public void handleResponse(List<Message> response) {
                Log.d(TAG, "successfully got some messages " + response.toString());

                for (Message message : response) {

                    fatFriends.add(message.getPublisherId());

                    BackendlessUser friend = DataHolder.getDataHolder().getFriend(message.getPublisherId());
                    ChatMessage chatMessage = new ChatMessage(message.getPublisherId(), (String) message.getData(),
                            (String) friend.getProperty("profileImageURL"), ChatMessage.ChatMessageType.RECIEVED);
                    DataHolder.getDataHolder().addMessage(chatMessage);
                }

                adapter.OnNewMessages(fatFriends);


            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "failed to get messages " + fault.getMessage());
            }
        }, new AsyncCallback<Subscription>() {
            @Override
            public void handleResponse(Subscription response) {
                Log.d(TAG, "successfully subscribed to individual channel");
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "failed to subscribe for individual channel " + fault.getMessage());
            }
        });
    }

    private void onLogoutClick() {

        Backendless.Messaging.subscribe(Backendless.UserService.CurrentUser().getEmail(), new AsyncCallback<List<Message>>() {
            @Override
            public void handleResponse(List<Message> response) {

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "failed to get messages " + fault.getMessage());
            }
        }, new AsyncCallback<Subscription>() {
            @Override
            public void handleResponse(Subscription response) {
                Log.d(TAG, "successfully unsubscribed from individual channel");
                Subscription subscription = response;
                subscription.cancelSubscription();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "failed to subscribe for individual channel " + fault.getMessage());
            }
        });

        Backendless.UserService.logout(new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void response) {
                Log.d(TAG + " FAT", "successfully logged out");
                finish();
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });

    }

    private void onFriendClick(int position) {
        String friendId = friends.get(position).getObjectId();

        if (fatFriends != null) {
            fatFriends.remove(friendId);
            Log.d(TAG, "friend id: " + friendId);
            Log.d(TAG, "removed from fatFriends " + fatFriends);
        }

        Intent intent = new Intent(this, FriendChatActivity.class);
        intent.putExtra("friendId", friendId);
        startActivity(intent);
    }

    private void onAddFriendClick() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);
        alert.setMessage("Add Friend");
        alert.setTitle("Enter Email");
        alert.setView(editText);

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email = editText.getText().toString();
                addFriend(email);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing
            }
        });

        alert.show();
    }

    private void addFriend(final String email) {
        BackendlessDataQuery query = new BackendlessDataQuery();

        query.setWhereClause("email = " + "'" + email + "'");

        Backendless.Data.of(BackendlessUser.class).find(query, new AsyncCallback<BackendlessCollection<BackendlessUser>>() {
            @Override
            public void handleResponse(BackendlessCollection<BackendlessUser> response) {

                notEqual = false;

                Log.d(TAG, "users are " + response.getData()); //no user or one user
                BackendlessUser user = Backendless.UserService.CurrentUser();

                //CHECKER FOR VALID EMAILS
                if (user.getEmail().equals(email)) {
                    Log.d(TAG, "CAN'T PUT OWN EMAIL");

                    notEqual = true;
                }
                for (int i = 0; i < friends.size(); i++) {
                    if (friends.get(i).getEmail().equals(email)) {
                        Log.d(TAG, "EMAIL is ALREADY IN THE LIST");
                        notEqual = true;
                        break;
                    }
                }

                if (!notEqual && response.getData().size() > 0) {
                    addFriend(response.getData().get(0));
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "failed to get user " + fault.getMessage());
            }
        });
    }

    private void addFriend(BackendlessUser friendUser) {

        FriendShip friendShip = new FriendShip();
        friendShip.setUser1(Backendless.UserService.CurrentUser());
        friendShip.setUser2(friendUser);

        Backendless.Data.of(FriendShip.class).save(friendShip, new AsyncCallback<FriendShip>() {
            @Override
            public void handleResponse(FriendShip response) {
                Log.d(TAG, "friendship saved successfully");
                getFriends();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "failed to save friendship relationship " + fault.getMessage());
            }
        });
    }

    private void getFriends() {
        //will download friendship relationships

        BackendlessUser user = Backendless.UserService.CurrentUser();
        String whereClause = "user1.objectId = " + "'" + user.getObjectId() + "'" + " OR " +
                "user2.objectId = " + "'" + user.getObjectId() + "'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);

        QueryOptions queryOptions = new QueryOptions();
        queryOptions.addRelated("user1");
        queryOptions.addRelated("user2");
        dataQuery.setQueryOptions(queryOptions);

        Backendless.Data.of(FriendShip.class).find(dataQuery, new AsyncCallback<BackendlessCollection<FriendShip>>() {
            @Override
            public void handleResponse(BackendlessCollection<FriendShip> response) {
                Log.d(TAG, "got friends " + response.getData());

                //TODO: display list of friends

                ArrayList<BackendlessUser> friends = new ArrayList<BackendlessUser>();
                BackendlessUser selfUser = Backendless.UserService.CurrentUser();
                for (FriendShip friendShip : response.getData()) {
                    if (friendShip.getUser1().getObjectId().equals(selfUser.getObjectId())) {
                        friends.add(friendShip.getUser2());
                    } else {
                        friends.add(friendShip.getUser1());
                    }
                }
                displayFriends(friends);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "failed to get friendship relations " + fault.getMessage());
            }
        });
    }

    private void displayFriends(ArrayList<BackendlessUser> friends) {
        DataHolder.getDataHolder().setFriends(friends);


        this.friends = friends;

        this.adapter = new FriendsAdapter(this, friends);
        listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();
        if (adapter != null) {
            Log.d(TAG, "your fat friends: " + fatFriends);
            adapter.OnNewMessages(fatFriends);
            Log.d(TAG + " Resume", "In ONResume");
        }
    }
}
