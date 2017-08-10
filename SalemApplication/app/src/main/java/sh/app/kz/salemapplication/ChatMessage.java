package sh.app.kz.salemapplication;

/**
 * Created by askhat on 19.06.16.
 */
public class ChatMessage {

    public enum ChatMessageType {
        SENT, RECIEVED
    }

    private String friendId;
    private String text;
    private String image;
    private ChatMessageType type;

    public ChatMessage(String friendId, String text, String image, ChatMessageType type) {
        this.friendId = friendId;
        this.text = text;
        this.image = image;
        this.type = type;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ChatMessageType getType() {
        return type;
    }

    public void setType(ChatMessageType type) {
        this.type = type;
    }


}
