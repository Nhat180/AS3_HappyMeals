package com.example.as3_happymeals.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.as3_happymeals.HttpHandler;
import com.example.as3_happymeals.MapsActivity;
import com.example.as3_happymeals.model.Comment;
import com.example.as3_happymeals.R;
import com.example.as3_happymeals.adapter.CommentAdapter;
import com.example.as3_happymeals.model.Site;
import com.example.as3_happymeals.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentFragment extends Fragment {
    private TextView textView;
    private EditText commentText;
    private ListView listView;
    private ImageView sendBtn;
    private List<Comment> arrayList = new ArrayList<>();
    private CommentAdapter adapter;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Site site = MapsActivity.site;
    private User user = MapsActivity.user;
    private boolean isValid = true;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.comment_fragment, container, false);
        listView = root.findViewById(R.id.list);
        commentText = root.findViewById(R.id.commentInput);
        sendBtn = root.findViewById(R.id.send);

        db.collection("sites").document(site.getLeaderUid())
                .collection("comments").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Comment comment = documentSnapshot.toObject(Comment.class);
                                arrayList.add(comment);
                            }
                            adapter = new CommentAdapter(getActivity(), R.layout.comment_layout, arrayList);
                            listView.setAdapter(adapter);
                        } else {
                            Toast.makeText(getContext(), "Error getting comment",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//        arrayList.add(new Comment(1,"Tong Nguyen", "Hello this site very good"));
//        arrayList.add(new Comment(2,"Thong Nguyen", " very good"));
//
//        adapter = new CommentAdapter(getActivity(), R.layout.comment_layout, arrayList);
//        listView.setAdapter(adapter);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), site.getLeaderUid(), Toast.LENGTH_SHORT).show();
                checkField(commentText);
                if (isValid) {
                    String text = commentText.getText().toString();
                    DocumentReference df = db.collection("sites").document(site.getLeaderUid())
                            .collection("comments").document(String.valueOf(site.getTotalComment()));
                    Map<String,Object> commentData = new HashMap<>();
                    commentData.put("id", df.getId());
                    commentData.put("name", user.getUserName());
                    commentData.put("textCom", text);
                    df.set(commentData);

                    arrayList.add(new Comment(String.valueOf(site.getTotalComment()),user.getUserName(), text));
                    adapter = new CommentAdapter(getActivity(), R.layout.comment_layout, arrayList);
                    listView.setAdapter(adapter);

                    commentText.getText().clear();
                    site.setTotalComment(site.getTotalComment() + 1);
                    new PutSite().execute();
                }
            }
        });


        return root;
    }

    // Check if users have input the data
    private boolean checkField (EditText editText) {
        if(editText.getText().toString().isEmpty()) {
            editText.setError("Required");
            isValid = false;
        }

        return isValid;
    }



    private class PutSite extends AsyncTask<Void, Void, Void> {
        private String status = "";
        @Override
        protected Void doInBackground(Void... voids) {
            status = HttpHandler.putRequest(MapsActivity.SITE_API_URL + "/" + site.getLeaderUid(), site);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}