package com.example.yumyumtree.ui.profile;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yumyumtree.R;
import com.example.yumyumtree.data.api.UserProfileHandler;
import com.example.yumyumtree.ui.home.HomeFragment;
import com.example.yumyumtree.ui.login.LoginFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;
import static com.example.yumyumtree.ui.login.LoginFragment.CURRENT_NAME;


public class ProfileFragment extends Fragment {

    private final UserProfileHandler userProfileHandler = UserProfileHandler.getInstance();
    private View view;
    private EditText editText;
    private ImageView profilePicture;
    private ProgressBar progressBar;
    private final DatabaseReference root = FirebaseDatabase.getInstance().getReference("users").child(CURRENT_NAME);
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_profile, container, false);
        createUIElements();
        return view;
    }

    private void createUIElements() {
        TextView fullName, email, phone;

        fullName = view.findViewById(R.id.fullname_profile);
        email = view.findViewById(R.id.email_profile);
        phone = view.findViewById(R.id.phone_profile);

        profilePicture = view.findViewById(R.id.profilePicture);
        if (userProfileHandler.getImageUrl() != null) {
            Glide.with(getContext()).load(userProfileHandler.getImageUrl()).into(profilePicture);
        }

        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);


        fullName.setText(userProfileHandler.getUserHelper().getFullName());
        email.setText(userProfileHandler.getUserHelper().getEmail());
        phone.setText(userProfileHandler.getUserHelper().getPhoneNo());

        view.findViewById(R.id.logout_button).setOnClickListener(logoutClickListener);
        view.findViewById(R.id.change_password_button).setOnClickListener(changePasswordClickListener);
        view.findViewById(R.id.profilePicture).setOnClickListener(profilePictureClickListener);
        view.findViewById(R.id.upload_button).setOnClickListener(uploadButtonClickListener);
    }

    private final View.OnClickListener uploadButtonClickListener = v -> {
        if (imageUri != null) {
            uploadToFirebase(imageUri);
        } else  {
            Toast.makeText(getActivity(), "Please select image!", Toast.LENGTH_SHORT).show();
        }
    };

    private void uploadToFirebase(Uri uri) {
        StorageReference fileReference = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ImageUrl imageUrl = new ImageUrl(uri.toString());
                        root.child("profileImage").setValue(imageUrl);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "Uploaded successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "Uploading failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private final View.OnClickListener profilePictureClickListener = v -> {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 2);
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            profilePicture.setImageURI(imageUri);
        }
    }

    private final View.OnClickListener logoutClickListener = v -> {
        LoginFragment loginFragment = new LoginFragment();
        loadFragments(loginFragment);
    };

    private final View.OnClickListener changePasswordClickListener = v -> {
          AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
          final EditText editText1 = new EditText(getContext());
          builder.setTitle(R.string.alert_message);
          builder.setView(editText1);

          LinearLayout linearLayout = new LinearLayout(getContext());
          linearLayout.setOrientation(LinearLayout.VERTICAL);
          linearLayout.addView(editText1);
          builder.setView(linearLayout);
          
          builder.setNegativeButton(R.string.negative_alert_button, (dialog, which) -> dialog.cancel());
          
          builder.setPositiveButton(R.string.positive_alert_button, (dialog, which) -> {
              editText = editText1;
              collectInput();
          });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    };
    
    private void collectInput() {
        DatabaseReference rootRef;
        String getInput = editText.getText().toString();
        String passwordValue = "^" +
                "(?=.*[a-zA-Z])" +
                "(?=.*[@#$%^&+=!])" +
                "(?=\\S+$)" +
                ".{4,}" +
                "$";
        if (getInput.trim().equals("")) {
            Toast.makeText(getActivity(), "Please add a new password", Toast.LENGTH_SHORT).show();
        } else if (!getInput.matches(passwordValue)) {
            Toast.makeText(getActivity(), "Password is to weak!", Toast.LENGTH_SHORT).show();
        } else {
            rootRef = FirebaseDatabase.getInstance().getReference("users");
            rootRef.child(CURRENT_NAME).child("password").setValue(getInput);
        }
    }

    public void loadFragments(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStackImmediate();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.loginactivity, fragment).commit();
    }

    public boolean onBackPressed() {
        HomeFragment homeFragment = new HomeFragment();
        loadFragments(homeFragment);
        return true;
    }
}