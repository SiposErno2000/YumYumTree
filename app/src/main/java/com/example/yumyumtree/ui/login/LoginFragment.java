package com.example.yumyumtree.ui.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yumyumtree.R;
import com.example.yumyumtree.ui.home.HomeFragment;
import com.example.yumyumtree.ui.register.RegisterFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {
    private FirebaseAuth auth;
    FirebaseDatabase root;
    DatabaseReference reference;
    TextInputLayout fullName, passwordText;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_login, container, false);
        result.findViewById(R.id.signup_button).setOnClickListener(signUpButtonClickListener);
        result.findViewById(R.id.login_button).setOnClickListener(loginButtonClickListener);

        fullName = result.findViewById(R.id.fullname);
        passwordText = result.findViewById(R.id.password);

        return result;
    }

    private View.OnClickListener signUpButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RegisterFragment registerFragment = new RegisterFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStackImmediate();
            fragmentManager.beginTransaction().addToBackStack(null).add(R.id.loginactivity, registerFragment).commit();
        }
    };

    private View.OnClickListener loginButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            auth = FirebaseAuth.getInstance();
            loginUserAccount();
        }
    };

    private void loginUserAccount() {
        String name, password;
        name = fullName.getEditText().getText().toString();
        password = passwordText.getEditText().getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getActivity(), "Please enter name!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Please enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        root = FirebaseDatabase.getInstance();
        reference = root.getReference("users").child(name);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentName = snapshot.child("fullName").getValue().toString();
                String currentPassword = snapshot.child("password").getValue().toString();

                if ( name.equals(currentName) && password.equals(currentPassword)) {
                    HomeFragment homeFragment = new HomeFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.popBackStackImmediate();
                    fragmentManager.beginTransaction().addToBackStack(null).add(R.id.loginactivity, homeFragment).commit();
                } else {
                    Toast.makeText(getActivity(), "Wrong credentials!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}