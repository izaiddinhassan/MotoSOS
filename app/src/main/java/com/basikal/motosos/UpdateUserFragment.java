package com.basikal.motosos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class UpdateUserFragment extends Fragment implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 123;
    private EditText mNameView, mPhoneNoView, mAddressView;
    private TextView mCancelView, mUpdateView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button mSelectImageButton;
    private Uri mFilePath;
    private ImageView mProfileImage;

    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private StorageReference mImageRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Update Profile Information");

        //create instances
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
        mImageRef = mStorageRef.child("images/profile/" + mAuth.getCurrentUser().getUid() + ".jpg");

        //data from fragment
        Bundle mArgs = getArguments();
        String name = mArgs.getString("name");
        String phoneNo = mArgs.getString("phoneNo");
        String address = mArgs.getString("address");

        //link with xml
        mNameView = view.findViewById(R.id.etName);
        mPhoneNoView = view.findViewById(R.id.etPhoneNo);
        mAddressView = view.findViewById(R.id.etAddress);
        mCancelView = view.findViewById(R.id.tvCancel);
        mUpdateView = view.findViewById(R.id.tvUpdate);
        mSelectImageButton = view.findViewById(R.id.btnSelectImage);
        mProfileImage = view.findViewById(R.id.ivProfilePicture);

        //give value to display
        mNameView.setText(name);
        mPhoneNoView.setText(phoneNo);
        mAddressView.setText(address);

        //attach listener
        mCancelView.setOnClickListener(this);
        mUpdateView.setOnClickListener(this);
        mSelectImageButton.setOnClickListener(this);

        GlideApp.with(this)
                .load(mImageRef)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(mProfileImage);
    }

    private void updateUser() {
        String uid = mAuth.getCurrentUser().getUid();
        String email = mAuth.getCurrentUser().getEmail();
        String name = mNameView.getText().toString().trim();
        String phoneNo = mPhoneNoView.getText().toString().trim();
        String address = mAddressView.getText().toString().trim();

        User user = new User(uid, name, email, phoneNo, address, null, null, null, null, null, null);
        Map<String, Object> postValues = user.toMap();
        mDatabase.child("User").child(uid).updateChildren(postValues).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Information Successfully Updated", Toast.LENGTH_SHORT).show();
                    uploadFile();
                } else {
                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mFilePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mFilePath);
                mProfileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void uploadFile() {
        final String uid = mAuth.getUid();
        if (mFilePath != null) {
            mStorageRef.child("images/profile/" + uid + ".jpg").putFile(mFilePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    new ProfileFragment()).commit();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment()).commit();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mCancelView) {
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment()).commit();
        } else if (v == mUpdateView) {
            updateUser();
        } else if (v == mSelectImageButton) {
            showFileChooser();
        }
    }

}
