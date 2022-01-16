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
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.as3_happymeals.HomePageActivity;
import com.example.as3_happymeals.LoginActivity;
import com.example.as3_happymeals.MapsActivity;
import com.example.as3_happymeals.R;
import com.example.as3_happymeals.SplashActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginTabFragment extends Fragment {
    private EditText email, pass, contactEmail;
    private TextView fPass, title;
    private Button login, backBtn;
    private float i=0;
    private boolean isValid = true; // Check value
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Button sendBtn, cancelButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_fragment, container, false);
        title = root.findViewById(R.id.title_login);
        email = (EditText) root.findViewById(R.id.email);
        pass = (EditText) root.findViewById(R.id.pass);
        fPass = root.findViewById(R.id.forget_pass);
        login = root.findViewById(R.id.login);
        backBtn = root.findViewById(R.id.backBtn);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isValidEmail(email);
                checkField(pass);

                if(isValid) {
                    String emailStr = email.getText().toString();
                    String passStr = pass.getText().toString();
                    firebaseAuth.signInWithEmailAndPassword(emailStr,passStr)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                        Toast.makeText(getContext(),
                                                "Logged Successfully", Toast.LENGTH_SHORT)
                                                .show();
                                        Intent intent = new Intent(getActivity(), MapsActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        getActivity().finish();
                                    } else {
                                        Toast.makeText(getContext(),
                                                "Please verify your email address", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),
                                    "Fail to login", Toast.LENGTH_SHORT).show();
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


        fPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog builder = new AlertDialog.Builder(getActivity()).create();
                View form = LayoutInflater.from(getActivity()).inflate(R.layout.forget_pass_dialog_layout, null);
                builder.setView(form);

                contactEmail = form.findViewById(R.id.contactEmail);
                sendBtn = form.findViewById(R.id.sendBtn);
                cancelButton = form.findViewById(R.id.cancelBtn);


                sendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(getActivity(),"Verification send",Toast.LENGTH_SHORT).show();
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(),"Cancel",Toast.LENGTH_SHORT).show();
                        builder.dismiss();
                    }
                });
                builder.show();
            }
        });
        // new


        // Set animation
        title.setTranslationX(800);
        email.setTranslationX(800);
        pass.setTranslationX(800);
        fPass.setTranslationX(800);
        login.setTranslationX(800);
        backBtn.setTranslationX(800);

        title.setAlpha(i);
        email.setAlpha(i);
        pass.setAlpha(i);
        fPass.setAlpha(i);
        login.setAlpha(i);
        backBtn.setAlpha(i);

        title.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(100).start();
        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(200).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        fPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(400).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        backBtn.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(600).start();

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


    // Check if users have input the data
    private boolean checkField (EditText text) {
        if(text.getText().toString().isEmpty()) {
            text.setError("Required");
            isValid = false;
        }

        return isValid;
    }
}
