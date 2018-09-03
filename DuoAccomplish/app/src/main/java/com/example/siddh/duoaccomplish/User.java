package com.example.siddh.duoaccomplish;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

    public static final int FRIEND_ALREADY_EXISTS = 1;

    private static User mInstance;

    private String mName;
    private String mEmail;
    private String mPassword;

    private UUID mId;

    private List<UUID> friends = new ArrayList<>();

    private User() {
        mId = UUID.randomUUID();
    }

    public static User get() {

        if(mInstance == null) {
            mInstance = new User();
        }

        return mInstance;
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
