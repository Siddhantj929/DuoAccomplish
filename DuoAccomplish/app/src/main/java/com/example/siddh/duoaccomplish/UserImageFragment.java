package com.example.siddh.duoaccomplish;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserImageFragment extends Fragment
        implements DBHandler.UserImageUploadedListener {

    private static final int REQUEST_PHOTO = 0;
    private static final String DIALOG_LOADING = "LoadingDialog";

    private String dialogText;

    private DBHandler mDBHandler;

    MaterialButton mNoImageButton;
    MaterialButton mUserImageButton;

    FloatingActionButton mCameraFAB;

    ImageView mUserImageView;

    TextView mNameTextView;

    private File mPhotoFile;

    private Callbacks mCallbacks;

    public static UserImageFragment newInstance() {
        return new UserImageFragment();
    }

    public interface Callbacks {
        void switchToActivity(Class<? extends Activity> activityToOpen);
    }

    @Override
    public void onUserImageUploaded() {
        mCallbacks.switchToActivity(MainUserPanelActivity.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPhotoFile = User.get().getPhotoFile();

        mDBHandler = new DBHandler();

        mDBHandler.setUserImageUploadedListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_userimage, container, false);

        mNoImageButton = (MaterialButton) v.findViewById(R.id.noImage_MaterialButton);
        mNoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.switchToActivity(MainUserPanelActivity.class);
            }
        });

        mUserImageButton = (MaterialButton) v.findViewById(R.id.userImage_MaterialButton);
        mUserImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager manager = getFragmentManager();
                new LoadingFragment()
                        .setLoadingText(dialogText)
                        .show(manager, DIALOG_LOADING);

                mDBHandler.uploadUserImage(getActivity());
            }
        });

        // Camera Capture Intent
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(getActivity().getPackageManager()) != null;

        Uri uri = FileProvider.getUriForFile(getActivity(),
                "com.example.siddh.duoaccomplish.fileprovider",
                mPhotoFile);

        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        List<ResolveInfo> cameraActivities = getActivity()
                .getPackageManager().queryIntentActivities(captureImage,
                        PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo activity : cameraActivities) {

            getActivity().grantUriPermission(activity.activityInfo.packageName,
                    uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        mCameraFAB = (FloatingActionButton) v.findViewById(R.id.camera_fab);
        mCameraFAB.setEnabled(canTakePhoto);

        mCameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mUserImageView = (CircleImageView) v.findViewById(R.id.userImageView);

        mNameTextView = (TextView) v.findViewById(R.id.user_name);
        // Set user's name as text
        if (!TextUtils.isEmpty(User.get().getName())) {
            mNameTextView.setText(User.get().getName());
        }

        updateUserImageView();

        return v;
    }

    private void updateUserImageView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {

            mNoImageButton.setVisibility(View.VISIBLE);
            mUserImageButton.setVisibility(View.GONE);

            mUserImageView.setImageDrawable(getActivity().
                    getResources().getDrawable(R.drawable.camera_placeholder));

        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());

            mUserImageView.setImageBitmap(bitmap);

            mNoImageButton.setVisibility(View.GONE);
            mUserImageButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dialogText = getActivity().getResources().getString(R.string.syncing_db);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.example.siddh.duoaccomplish.fileprovider",
                    mPhotoFile);

            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updateUserImageView();
        }
    }
}
