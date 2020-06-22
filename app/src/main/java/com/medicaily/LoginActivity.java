package com.medicaily;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int MY_REQUEST_CODE = 1127;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<AuthUI.IdpConfig> providers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Init providers
        providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),   //Google Builder
                new AuthUI.IdpConfig.FacebookBuilder().build(), //Facebook Builder
                //new AuthUI.IdpConfig.TwitterBuilder().build(),  //Twitter Builder
                //new AuthUI.IdpConfig.GitHubBuilder().build(),   //GitHub Builder
                new AuthUI.IdpConfig.EmailBuilder().build(),    //Email Builder
                new AuthUI.IdpConfig.PhoneBuilder().build(),    //Phone Builder
                new AuthUI.IdpConfig.AnonymousBuilder().build() //Anonymous Builder


        );

        showSignInOptions();
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .setLogo(R.mipmap.medicaily)
                        .setTheme(R.style.MyTheme)
                        .build(), MY_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                //Get user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //Get the current user auth id and set it to current user id
                final String currentUserUid = user.getUid();

                FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                CollectionReference usersRef = rootRef.collection("users");
                usersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // loops through the users collection to check if user exists
                            for (DocumentSnapshot document : task.getResult()) {
                                String userId = document.getString("userId");
                                if (userId.equals(currentUserUid)) {
                                    openMainActivity();
                                    Toast.makeText(LoginActivity.this, "Welcome Back", Toast.LENGTH_SHORT).show();
                                } else {
                                    //add user to the user table with the auth.uid as user id for new user
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("userId",currentUserUid);
                                    db.collection("users")
                                            .add(data)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    openMainActivity();
                                                    Toast.makeText(LoginActivity.this, "user saved", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(LoginActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                                    Log.d(TAG, e.toString());
                                                }
                                            });
                                }
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

            } else {
                Toast.makeText(this, "" + response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
