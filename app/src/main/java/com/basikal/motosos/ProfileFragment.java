package com.basikal.motosos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private TextView mNameView, mEmailView, mIcNoView, mPhoneNoView, mAddressView, mDobView,
            mGenderView, mBloodType, mInsurancePolicy, mInsurancePhone;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FloatingActionButton mEditButton;
    private ImageView mProfileImage;

    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private StorageReference mImageRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // do your variables initialisations here except Views!!!
        mDatabase = DbConn.getDatabase().getReference();
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
        mImageRef = mStorageRef.child("images/profile/" + mAuth.getCurrentUser().getUid() + ".jpg");
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("User Information");

        // initialise your views
        mNameView = view.findViewById(R.id.tvName);
        mEmailView = view.findViewById(R.id.tvEmail);
        mIcNoView = view.findViewById(R.id.tvIcNo);
        mDobView = view.findViewById(R.id.tvDob);
        mPhoneNoView = view.findViewById(R.id.tvPhoneNo);
        mAddressView = view.findViewById(R.id.tvAddress);
        mGenderView = view.findViewById(R.id.tvGender);
        mBloodType = view.findViewById(R.id.tvBloodType);
        mInsurancePolicy = view.findViewById(R.id.tvInsPolicyNumber);
        mInsurancePhone = view.findViewById(R.id.tvInsPhoneNumber);
        mEditButton = view.findViewById(R.id.fabEdit);
        mProfileImage = view.findViewById(R.id.ivProfilePicture);


        mEditButton.setEnabled(false);
        mEditButton.setOnClickListener(this);



        //load picture into image view
        GlideApp.with(this)
                .load(mImageRef)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(mProfileImage);

        getUserDataFromDb();
    }

    public void getUserDataFromDb() {
        String uid = mAuth.getUid();
        mDatabase.child("User")
                .child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            User user = dataSnapshot.getValue(User.class);
                            String name = user.name;
                            mNameView.setText(user.name);
                            mEmailView.setText(user.email);
                            mIcNoView.setText(user.icNo);
                            mPhoneNoView.setText(user.phoneNo);
                            mAddressView.setText(user.address);
                            mDobView.setText(user.dob);
                            mGenderView.setText(user.gender);
                            mBloodType.setText(user.bloodType);
                            mInsurancePolicy.setText(user.insurancePolicy);
                            mInsurancePhone.setText(user.insurancePhone);

                            mEditButton.setEnabled(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void updateUserProfile() {
        Bundle bundle = new Bundle();
        bundle.putString("name", mNameView.getText().toString().trim());
        bundle.putString("icNo", mIcNoView.getText().toString().trim());
        bundle.putString("phoneNo", mPhoneNoView.getText().toString().trim());
        bundle.putString("address", mAddressView.getText().toString().trim());
        bundle.putString("dob", mDobView.getText().toString().trim());
        bundle.putString("gender", mGenderView.getText().toString().trim());
        bundle.putString("bloodType", mBloodType.getText().toString().trim());
        bundle.putString("insurancePolicy", mInsurancePolicy.getText().toString().trim());
        bundle.putString("insurancePhoneNo", mInsurancePhone.getText().toString().trim());
        UpdateUserFragment updateUserFragment = new UpdateUserFragment();
        updateUserFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, updateUserFragment, "FRAG_UPDATE_USER")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        if (v == mEditButton) {
            updateUserProfile();
        }
    }
}
