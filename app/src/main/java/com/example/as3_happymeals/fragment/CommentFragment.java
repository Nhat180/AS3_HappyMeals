package com.example.as3_happymeals.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.as3_happymeals.model.Comment;
import com.example.as3_happymeals.R;
import com.example.as3_happymeals.adapter.CommentAdapter;


import java.util.ArrayList;
import java.util.List;

public class CommentFragment extends Fragment {
    private TextView textView;
    private ListView listView;
    private List<Comment> arrayList;
    private CommentAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.comment_fragment, container, false);
        listView = root.findViewById(R.id.list);

        arrayList = new ArrayList<>();
        arrayList.add(new Comment(1,"Tong Nguyen", "Hello this site very good"));
        arrayList.add(new Comment(2,"Thong Nguyen", " very good"));

        adapter = new CommentAdapter(getActivity(), R.layout.comment_layout, arrayList);
        listView.setAdapter(adapter);



        return root;
    }
}