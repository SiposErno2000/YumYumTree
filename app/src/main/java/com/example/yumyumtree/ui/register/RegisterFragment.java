package com.example.yumyumtree.ui.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yumyumtree.R;
import com.example.yumyumtree.ui.home.HomeFragment;
import com.example.yumyumtree.ui.login.LoginFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegisterFragment extends Fragment {

    private TextInputLayout fullName, username, email, phoneNo, password;
    private List<String> users;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_register, container, false);

        fullName = result.findViewById(R.id.fullname);
        username = result.findViewById(R.id.username);
        email = result.findViewById(R.id.email);
        phoneNo = result.findViewById(R.id.phonenumber);
        password = result.findViewById(R.id.password);

        result.findViewById(R.id.registerbutton).setOnClickListener(registerButtonClickListener);
        result.findViewById(R.id.already).setOnClickListener(backToLoginClickListener);

        users = existingUsers();

        return result;
    }

    private List<String> existingUsers() {
        List<String> users = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            UserHelper userHelper = dataSnapshot.getValue(UserHelper.class);
                            if (userHelper != null) {
                                users.add(userHelper.getFullName());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        return users;
    }

    private Boolean validateName() {
        String value = fullName.getEditText().getText().toString();
        for (String user : users) {
            if (user.equals(value)) {
                Toast.makeText(getActivity(), "Name is already exist!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (value.isEmpty()) {
            Toast.makeText(getActivity(), "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            fullName.setError(null);
            fullName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername() {
        String value = username.getEditText().getText().toString();
        String whiteSpaces = "\\A\\w{4,20}\\z";

        if (value.isEmpty()) {
            Toast.makeText(getActivity(), "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (value.length() >= 20) {
            Toast.makeText(getActivity(), "Username too long!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!value.matches(whiteSpaces)) {
            Toast.makeText(getActivity(), "White Spaces are not allowed!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    private Boolean validateEmail() {
        String value = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";

        if (value.isEmpty()) {
            Toast.makeText(getActivity(), "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!value.matches(emailPattern)) {
            Toast.makeText(getActivity(), "Invalid email!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String value = phoneNo.getEditText().getText().toString();

        if (value.isEmpty()) {
            Toast.makeText(getActivity(), "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            phoneNo.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String value = password.getEditText().getText().toString();
        String passwordValue = "^" +
                "(?=.*[a-zA-Z])" +
                "(?=.*[@#$%^&+=!])" +
                "(?=\\S+$)" +
                ".{4,}" +
                "$";

        if (value.isEmpty()) {
            Toast.makeText(getActivity(), "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!value.matches(passwordValue)) {
            Toast.makeText(getActivity(), "Password is to weak!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    private final View.OnClickListener registerButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!validateName() || !validateUsername() || !validateEmail() || !validatePhoneNo() || !validatePassword()) {
                return;
            }
            FirebaseDatabase root = FirebaseDatabase.getInstance();
            DatabaseReference reference = root.getReference("users");

            String name = fullName.getEditText().getText().toString();
            String user = username.getEditText().getText().toString();
            String mail = email.getEditText().getText().toString();
            String phone = phoneNo.getEditText().getText().toString();
            String pass = password.getEditText().getText().toString();

            UserHelper userHelper = new UserHelper(name, user, mail, phone, pass);

            reference.child(name).setValue(userHelper);

            HomeFragment homeFragment = new HomeFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStackImmediate();
            fragmentManager.beginTransaction().addToBackStack(null).add(R.id.loginactivity, homeFragment).commit();
        }
    };

    private final View.OnClickListener backToLoginClickListener = v -> {
        LoginFragment loginFragment = new LoginFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStackImmediate();
        fragmentManager.beginTransaction().addToBackStack(null).add(R.id.loginactivity, loginFragment).commit();
    };
}