package com.example.yumyumtree.ui.profile;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yumyumtree.R;
import com.example.yumyumtree.data.api.UserProfileHandler;
import com.example.yumyumtree.ui.home.HomeFragment;
import com.example.yumyumtree.ui.login.LoginFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.yumyumtree.data.api.UserProfileHandler.CHILD;
import static com.example.yumyumtree.ui.login.LoginFragment.CURRENT_NAME;


public class ProfileFragment extends Fragment {

    private static final String ALERT_MESSAGE = "Enter a new password!";
    private static final String NEGATIVE_BUTTON_TEXT = "Cancel";
    private static final String POSITIVE_BUTTON_TEXT = "Ok";
    private final UserProfileHandler userProfileHandler = UserProfileHandler.getInstance();
    private View view;
    private EditText editText;

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

        fullName.setText(userProfileHandler.getUserHelper().getFullName());
        email.setText(userProfileHandler.getUserHelper().getEmail());
        phone.setText(userProfileHandler.getUserHelper().getPhoneNo());

        view.findViewById(R.id.logout_button).setOnClickListener(logoutClickListener);
        view.findViewById(R.id.change_password_button).setOnClickListener(changePasswordClickListener);
    }

    private final View.OnClickListener logoutClickListener = v -> {
        LoginFragment loginFragment = new LoginFragment();
        loadFragments(loginFragment);
    };

    private final View.OnClickListener changePasswordClickListener = v -> {
          AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
          final EditText editText1 = new EditText(getContext());
          builder.setTitle(ALERT_MESSAGE);
          builder.setView(editText1);

          LinearLayout linearLayout = new LinearLayout(getContext());
          linearLayout.setOrientation(LinearLayout.VERTICAL);
          linearLayout.addView(editText1);
          builder.setView(linearLayout);
          
          builder.setNegativeButton(NEGATIVE_BUTTON_TEXT, (dialog, which) -> dialog.cancel());
          
          builder.setPositiveButton(POSITIVE_BUTTON_TEXT, (dialog, which) -> {
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
        if (getInput == null || getInput.trim().equals("")) {
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