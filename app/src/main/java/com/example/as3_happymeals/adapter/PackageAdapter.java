package com.example.as3_happymeals.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import com.example.as3_happymeals.R;
import com.example.as3_happymeals.model.Package;

public class PackageAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Package> arrayList;

    public PackageAdapter(Context context, int layout, List<Package> arrayList) {
        this.context = context;
        this.layout = layout;
        this.arrayList = arrayList;
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(layout, null);

        Package aPackage = arrayList.get(position);
        TextView text1 = convertView.findViewById(R.id.tv_name);
        TextView text2 = convertView.findViewById(R.id.tv_quantity);
        TextView text3 = convertView.findViewById(R.id.tv_description);

        text1.setText(aPackage.getPackageName());
        text2.setText(String.valueOf(aPackage.getQuantity()));
        text3.setText(aPackage.getDescription());

        return convertView;
    }
}
