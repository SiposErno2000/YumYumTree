package com.example.yumyumtree.data.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.yumyumtree.ui.register.UserHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.yumyumtree.ui.login.LoginFragment.CURRENT_NAME;

public class UserProfileHandler {

    private final static String TAG = UserProfileHandler.class.getSimpleName();
    public final static String CHILD = "favouriteList";
    private static UserProfileHandler userProfileHandler;
    private UserHelper userHelper;
    private ArrayList<String> favouriteList;

    private UserProfileHandler() {}

    public synchronized static UserProfileHandler getInstance() {
        if (userProfileHandler == null) {
            userProfileHandler = new UserProfileHandler();
        }
        return userProfileHandler;
    }

    public UserHelper getUserHelper() {
        return userHelper;
    }

    public void setUserHelper(UserHelper userHelper) {
        this.userHelper = userHelper;
    }

    public ArrayList<String> getFavouriteList() {
        return favouriteList;
    }

    public void setFavouriteList(ArrayList<String> favouriteList) {
        this.favouriteList = favouriteList;
    }

    public void getUserData() {
        FirebaseDatabase root = FirebaseDatabase.getInstance();
        DatabaseReference reference = root.getReference("users").child(CURRENT_NAME);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullName = snapshot.child("fullName").getValue().toString();
                String password = snapshot.child("password").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String username = snapshot.child("username").getValue().toString();
                String phoneNo = snapshot.child("phoneNo").getValue().toString();

                userHelper = new UserHelper(fullName, username, email, phoneNo, password);
                setUserHelper(userHelper);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Unsuccessful data request!");
            }
        });

        ArrayList<String> favouriteList = new ArrayList<>();
        reference = root.getReference("users").child(CURRENT_NAME).child(CHILD);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String id = ds.getKey();
                    favouriteList.add(id);
                }
                setFavouriteList(favouriteList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Unsuccessful data request!");
            }
        });
    }

    public void removeItemFromList(String item) {
        for (int i=0; i<favouriteList.size(); i++) {
            if (favouriteList.get(i).equals(item)) {
                favouriteList.remove(i);
                break;
            }
        }
    }
}
