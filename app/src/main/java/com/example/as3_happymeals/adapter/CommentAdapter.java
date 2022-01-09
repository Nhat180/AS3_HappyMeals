package com.example.as3_happymeals.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.as3_happymeals.model.Comment;
import com.example.as3_happymeals.R;

import java.util.List;

public class CommentAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Comment> arrayList;

    public CommentAdapter(Context context, int layout, List<Comment> arrayList) {
        this.context = context;
        this.layout = layout;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(layout, null);

        Comment comment = arrayList.get(position);
        TextView text1 = convertView.findViewById(R.id.nameUser);
        TextView text2 = convertView.findViewById(R.id.textComment);


        text1.setText(comment.getName());
        text2.setText(comment.getTextCom());

        return convertView;
    }
}