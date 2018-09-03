package com.example.siddh.duoaccomplish;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class DBHandler {

    private FirebaseFirestore db;
    private FirebaseStorage storage;

    private Context mContext;

    private CollectionReference userData;
    private StorageReference userStorage;

    private UserRegisteredListener mUserRegisteredListener;
    private UserImageUploadedListener mUserImageUploadedListener;

    private static final String NAME = "Name";
    private static final String EMAIL = "Email";
    private static final String PASSWORD = "Password";
    private static final String ID = "Id";
    private static final String DBNAME = "Users";

    public DBHandler() {
        db = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();

        userData = db.collection(DBNAME);

        StorageReference temp = storage.getReference();

        userStorage = temp.child(User.get().getPhotoFilename());
    }

    public void setContext(Context context) {
        mContext = context.getApplicationContext();
    }

    public void registerUser() {
        Log.i("Debug", "registerUser: Reached here!");
        new RegisterUserTask().execute();
    }

    public void uploadUserImage(Activity activity) {
        Log.i("Debug", "uploadUserImage: Reached here!");
        new UploadUserImageTask(activity).execute();
    }

    public interface UserRegisteredListener {
        void onUserRegistered();
    }

    public interface UserImageUploadedListener {
        void onUserImageUploaded();
    }

    public void setUserRegisteredListener(UserRegisteredListener userRegisteredListener) {
        mUserRegisteredListener = userRegisteredListener;
    }

    public void setUserImageUploadedListener(UserImageUploadedListener userImageUploadedListener) {
        mUserImageUploadedListener = userImageUploadedListener;
    }

    private class RegisterUserTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Map<String, Object> user = new HashMap<>();
            user.put(NAME, User.get().getName());
            user.put(EMAIL, User.get().getEmail());
            user.put(PASSWORD, User.get().getPassword());
            user.put(ID, User.get().getId().toString());

            userData.document(User.get().getId().toString())
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Succesful
                            Log.i("Debug", "onSuccess: Success in registration");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed :[
                            Log.e("Debug", "onFailure: Couldnt register", e);
                        }
                    });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mUserRegisteredListener.onUserRegistered();
        }
    }

    private class UploadUserImageTask extends AsyncTask<Void, Void, Void> {

        private Activity mActivity;

        public UploadUserImageTask(Activity activity) {
            mActivity = activity;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    User.get().getPhotoFile().getPath(), mActivity);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            byte[] data = outputStream.toByteArray();

            userStorage.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Success
                            Log.i("Debug", "onSuccess: Successfully uploaded image!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Debug", "onFailure: Couldnt upload image!", e);
                        }
                    });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mUserImageUploadedListener.onUserImageUploaded();
        }
    }
}
