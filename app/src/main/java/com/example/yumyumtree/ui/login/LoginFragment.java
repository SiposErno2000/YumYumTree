package com.example.yumyumtree.ui.login;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {

    private final static String TAG = LoginFragment.class.getSimpleName();
    public final static String USERS = "users";
    public static String CURRENT_NAME;
    private FirebaseAuth auth;
    private TextInputLayout fullName, passwordText;

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
        result.findViewById(R.id.forget_button).setOnClickListener(forgetButtonClickListener);

        fullName = result.findViewById(R.id.fullname);
        passwordText = result.findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();

        return result;
    }

    private final View.OnClickListener signUpButtonClickListener = v -> {
        RegisterFragment registerFragment = new RegisterFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStackImmediate();
        fragmentManager.beginTransaction().addToBackStack(null).add(R.id.loginactivity, registerFragment).commit();
    };

    private final View.OnClickListener loginButtonClickListener = v -> loginUserAccount();

    private final View.OnClickListener forgetButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText resetMail = new EditText(v.getContext());
            AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
            passwordResetDialog.setTitle("Reset password?");
            passwordResetDialog.setMessage("Enter your email to received reset link");
            passwordResetDialog.setView(resetMail);

            passwordResetDialog.setPositiveButton("Yes", (dialog, which) -> {
                String mail = resetMail.getText().toString();
                auth.sendPasswordResetEmail(mail).addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Reset link sent to your email!", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(getActivity(), "Error! Reset link is not sent!" + e.getMessage(), Toast.LENGTH_LONG).show());
            });

            passwordResetDialog.setNegativeButton("No", (dialog, which) -> {

            });

            passwordResetDialog.create().show();
        }
    };

    private void loginUserAccount() {
        String name;
        String password;
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

        FirebaseDatabase root = FirebaseDatabase.getInstance();
        DatabaseReference reference = root.getReference(USERS).child(name);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentName = snapshot.child("fullName").getValue().toString();
                String currentPassword = snapshot.child("password").getValue().toString();

                if ( name.equals(currentName) && password.equals(currentPassword)) {
                    CURRENT_NAME = currentName;
                    HomeFragment homeFragment = new HomeFragment();
                    FragmentTransaction fragmentTransaction = null;
                    if (getFragmentManager() != null) {
                        fragmentTransaction = getFragmentManager().beginTransaction();
                    }
                    if (fragmentTransaction != null) {
                        fragmentTransaction.addToBackStack(null).replace(R.id.loginactivity, homeFragment).commit();
                    }
                } else {
                    Toast.makeText(getActivity(), "Wrong credentials!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Unsuccessful data request!");
            }
        });
    }


    public boolean onBackPressed() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setMessage(R.string.alert_message_home);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.positive_alert_button_home, (dialog, which) -> System.exit(0));
        builder.setNegativeButton(R.string.negative_alert_button_home, (dialog, which) -> dialog.cancel());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return true;
    }
}