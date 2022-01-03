package com.example.as3_happymeals.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.as3_happymeals.HomePageActivity;
import com.example.as3_happymeals.LoginActivity;
import com.example.as3_happymeals.MapsActivity;
import com.example.as3_happymeals.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginTabFragment extends Fragment {
    private EditText email, pass;
    private TextView fPass, title;
    private Button login;
    private float i=0;
    private boolean isValid = true; // Check value
//    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_fragment, container, false);
        title = root.findViewById(R.id.title_login);
        email = (EditText) root.findViewById(R.id.email);
        pass = (EditText) root.findViewById(R.id.pass);
        fPass = root.findViewById(R.id.forget_pass);
        login = root.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MapsActivity.class));

            }
        });


        // Set animation
        title.setTranslationX(800);
        email.setTranslationX(800);
        pass.setTranslationX(800);
        fPass.setTranslationX(800);
        login.setTranslationX(800);

        title.setAlpha(i);
        email.setAlpha(i);
        pass.setAlpha(i);
        fPass.setAlpha(i);
        login.setAlpha(i);

        title.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(200).start();
        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        fPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();

        return root;
    }
}
