package com.example.siddh.duoaccomplish;

import android.content.Context;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

    public static final int FRIEND_ALREADY_EXISTS = 1;

    private static final String NAME = "Name";
    private static final String EMAIL = "Email";
    private static final String PASSWORD = "Password";
    private static final String ID = "Id";
    private static final String DBNAME = "Users";

    private static User mInstance;

    private String mName;
    private String mEmail;
    private String mPassword;

    private Context mContext;

    private UUID mId;

    private FirebaseFirestore db;

    private CollectionReference userData;

    private List<UUID> friends = new ArrayList<>();

    private User() {
        mId = UUID.randomUUID();
        db = FirebaseFirestore.getInstance();
        userData = db.collection(DBNAME);
    }

    public void setContext(Context context) {
        mContext = context.getApplicationContext();
    }

    public static User get() {

        if(mInstance == null) {
            mInstance = new User();
        }

        return mInstance;
    }

    public String getPhotoFilename() {
        return "IMG_" + mId.toString() + ".jpg";
    }

    public File getPhotoFile() {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, getPhotoFilename());
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public UUID getId() {
        return mId;
    }

    public int addFriend(UUID friendId) {

        if (friends.contains(friendId)) {
            return FRIEND_ALREADY_EXISTS;

        } else {
            friends.add(friendId);
            return 0;
        }
    }

    public List<UUID> getFriends() {
        return friends;
    }
}
