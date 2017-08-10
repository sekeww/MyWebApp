package sh.app.kz.salemapplication;

import android.provider.ContactsContract;

import com.backendless.BackendlessUser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by askhat on 19.06.16.
 */
public class DataHolder {

    private static DataHolder dataHolder;

    public static DataHolder getDataHolder() {
        if (dataHolder == null) {
            dataHolder = new DataHolder();
            dataHolder.friends = new ArrayList<>();
            dataHolder.messages = new ArrayList<>();
            dataHolder.listeners = new HashMap<>();
        }
        return dataHolder;
    }

    private ArrayList<BackendlessUser> friends;
    private ArrayList<ChatMessage> messages;
    private HashMap<String, MessagesListener> listeners;

    public void setFriends(ArrayList<BackendlessUser> friends) {
        this.friends = friends;
    }

    public BackendlessUser getFriend(String friendId) {
        for (BackendlessUser friend : friends) {
            if (friend.getObjectId().equals(friendId)) {
                return friend;
            }
        }
        return null;
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        if (listeners.get(message.getFriendId()) != null) {
            listeners.get(message.getFriendId()).onNewMessages();
        }
    }

    public ArrayList<ChatMessage> getFriendMessages(String friendId) {
        ArrayList<ChatMessage> friendMessages = new ArrayList<>();

        for (ChatMessage msg : this.messages) {
            if (msg.getFriendId().equals(friendId)) {
                friendMessages.add(msg);
            }
        }
        return friendMessages;
    }

    public void addListener(String friendId, MessagesListener listener) {
        listeners.put(friendId, listener);
    }

    public void removeListener(String friendId, MessagesListener listener) {
        listeners.remove(friendId);
    }
}
