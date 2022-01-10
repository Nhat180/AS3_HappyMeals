package com.example.as3_happymeals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.as3_happymeals.model.Package;

import java.text.DateFormat;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    RealmResults<Package> packageList;

    public MyAdapter(Context context, RealmResults<Package> packageList) {
        this.context = context;
        this.packageList = packageList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.package_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        Package packages = packageList.get(position);
        holder.nameOutput.setText(packages.getProductName());
        holder.quantityOutput.setText(packages.getQuantity());
        holder.ownerOutput.setText(packages.getOwner());
        holder.descriptionOutput.setText(packages.getDescription());

        String formatedTime = DateFormat.getDateTimeInstance().format(packages.getCreatedTime());
        holder.timeOutput.setText(formatedTime);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(context,view);
                menu.getMenu().add("DELETE");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getTitle().equals("DELETE")){
                            // Delete the package
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            packages.deleteFromRealm();
                            realm.commitTransaction();
                            Toast.makeText(context,"Package deleted",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nameOutput;
        TextView quantityOutput;
        TextView ownerOutput;
        TextView descriptionOutput;
        TextView timeOutput;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            nameOutput = itemView.findViewById(R.id.nameOutput);
            quantityOutput = itemView.findViewById(R.id.quantityOutput);
            ownerOutput = itemView.findViewById(R.id.ownerOutput);
            descriptionOutput = itemView.findViewById(R.id.descriptionOutput);
            timeOutput = itemView.findViewById(R.id.timeOutput);


        }

    }

}
