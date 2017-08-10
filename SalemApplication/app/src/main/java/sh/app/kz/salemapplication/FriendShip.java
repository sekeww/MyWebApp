package sh.app.kz.salemapplication;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

/**
 * Created by askhat on 17.06.16.
 */
public class FriendShip {

    private String objectId;
    private BackendlessUser user1;
    private BackendlessUser user2;


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public BackendlessUser getUser1() {
        return user1;
    }

    public void setUser1(BackendlessUser user1) {
        this.user1 = user1;
    }

    public BackendlessUser getUser2() {
        return user2;
    }

    public void setUser2(BackendlessUser user2) {
        this.user2 = user2;
    }
}
