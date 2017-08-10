package kz.sekeww.suretgram;

import com.backendless.BackendlessUser;

/**
 * Created by Askhat on 6/16/2016.
 */

public class Post {

    private String file;
    private String message;
    private BackendlessUser user;

    public Post() {
    }

    public Post(BackendlessUser user, String file, String message) {
        this.file = file;
        this.message = message;
        this.user = user;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BackendlessUser getUser() {
        return user;
    }

    public void setUser(BackendlessUser user) {
        this.user = user;
    }
}
