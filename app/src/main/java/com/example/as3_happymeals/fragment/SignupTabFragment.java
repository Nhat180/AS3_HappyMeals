package com.example.as3_happymeals.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.as3_happymeals.HomePageActivity;
import com.example.as3_happymeals.LoginActivity;
import com.example.as3_happymeals.MapsActivity;
import com.example.as3_happymeals.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SignupTabFragment extends Fragment {

    private EditText username,email,pass,rPass;
    private Button signup, backBtn;
    private TextView title;
    private int i=0;
    private boolean isValid = true; // Check value
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_fragment, container, false);
        title = root.findViewById(R.id.title_signup);
        username = (EditText) root.findViewById(R.id.username);
        email = (EditText) root.findViewById(R.id.email);
        pass = (EditText) root.findViewById(R.id.pass);
        rPass = (EditText) root.findViewById(R.id.re_pass);
        signup = root.findViewById(R.id.signup);
        backBtn = root.findViewById(R.id.backBtn);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isValidEmail(email);
                checkField(pass);
                checkPassLength(pass);
                checkField(rPass);

                // If all checks are correct, then allow users to create an account and sign in to the map
                if (isValid) {
                    String nameStr = username.getText().toString();
                    String emailStr = email.getText().toString();
                    String passStr = pass.getText().toString();
                    firebaseAuth.createUserWithEmailAndPassword(emailStr,passStr)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()) {
                                                        Toast.makeText(getActivity(),
                                                                "Registered successfully, Please check your email",
                                                                Toast.LENGTH_SHORT).show();
                                                        // Create data storing in the firestore
                                                        DocumentReference df = db.collection("users")
                                                                .document(user.getUid());
                                                        Map<String, Object> userData = new HashMap<>();
                                                        userData.put("userName", nameStr);
                                                        userData.put("email", emailStr);
                                                        userData.put("isAdmin", "0");
                                                        userData.put("siteRegistered", Collections.emptyList());
                                                        df.set(userData);
                                                        username.getText().clear();
                                                        email.getText().clear();
                                                        pass.getText().clear();
                                                        rPass.getText().clear();
//                                                      startActivity(new Intent(getActivity(),MapsActivity.class));
//                                                      getActivity().finish();
                                                    } else {
                                                        Toast.makeText(getActivity(),
                                                                task.getException().getMessage(),
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Failed to create the account",
                                        Toast.LENGTH_SHORT).show();
                                    }
                            });
                    }
                }
            });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HomePageActivity.class));
            }
        });

        title.setTranslationX(800);
        username.setTranslationX(800);
        email.setTranslationX(800);
        pass.setTranslationX(800);
        rPass.setTranslationX(800);
        signup.setTranslationX(800);
        backBtn.setTranslationX(800);

        title.setAlpha(i);
        username.setAlpha(i);
        email.setAlpha(i);
        pass.setAlpha(i);
        rPass.setAlpha(i);
        signup.setAlpha(i);
        backBtn.setAlpha(i);

        title.animate().translationX(0).alpha(1).setDuration(700).setStartDelay(100).start();
        username.animate().translationX(0).alpha(1).setDuration(700).setStartDelay(200).start();
        email.animate().translationX(0).alpha(1).setDuration(700).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(700).setStartDelay(400).start();
        rPass.animate().translationX(0).alpha(1).setDuration(700).setStartDelay(500).start();
        signup.animate().translationX(0).alpha(1).setDuration(700).setStartDelay(600).start();
        backBtn.animate().translationX(0).alpha(1).setDuration(700).setStartDelay(700).start();

        return root;
    }

    // Check if email is valid
    private boolean isValidEmail(EditText text) {
        if (!text.getText().toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(text.getText().toString()).matches()){
            isValid = true;
        }
        else {
            text.setError("Email is not valid");
            isValid = false;
        }
        return isValid;
    }

    // Check the password length, must over 6 characters
    private boolean checkPassLength(EditText text) {
        if (text.getText().toString().length() < 6) {
            text.setError("Password length must be above 6 characters in length");
            isValid = false;
        }
        return isValid;
    }

    // Check if users have input the data and if the "confirm password" is correct
    private boolean checkField (EditText text) {
        // Check data field is null or not
        if(text.getText().toString().isEmpty()) {
            text.setError("Required");
            isValid = false;
        }

        // Check "confirm" password
        if (!text.getText().toString().equals(pass.getText().toString())){
            text.setError("Password confirmation is wrong");
            isValid = false;
        }


        return isValid;
    }

}
