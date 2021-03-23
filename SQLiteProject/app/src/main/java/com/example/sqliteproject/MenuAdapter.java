package com.example.sqliteproject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private Activity activity;
    private Context context;
    private ArrayList<String> id, name;

    public MenuAdapter(Activity activity, Context context, ArrayList<String> id, ArrayList<String> name) {
        this.context = context;
        this.activity = activity;
        this.id = id;
        this.name = name;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_template, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.id.setText(String.valueOf(id.get(position)));
        holder.name.setText(String.valueOf(name.get(position)));
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, String.valueOf(id.get(position)), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(id.get(position)));
                intent.putExtra("name", String.valueOf(name.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });
    }


    @Override
    public int getItemCount() {
        return id.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout container;
        TextView id, name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.menu_id);
            name = itemView.findViewById(R.id.menu_name);
            container = itemView.findViewById(R.id.menu_container);
        }
    }
}
