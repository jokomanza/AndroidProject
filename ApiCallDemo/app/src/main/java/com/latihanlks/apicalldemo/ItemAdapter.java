package com.latihanlks.apicalldemo;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.zip.Inflater;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.Holder> implements Filterable {

    ArrayList<Items> items;
    ArrayList<Items> allItems;
    Context context;

    public ItemAdapter(ArrayList<Items> items, Context context) {
        this.items = items;
        this.context = context;
        this.allItems = new ArrayList<>(items);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Log.i("INFO", "onBindViewHolder: " + position);
        holder.txtId.setText(items.stream().map(Items::getId).collect(Collectors.toList()).get(position).toString());
        holder.txtName.setText(items.stream().map(Items::getName).collect(Collectors.toList()).get(position));
        holder.txtIsComplete.setText(items.stream().map(Items::isComplete).collect(Collectors.toList()).get(position).toString());

        holder.parent.setOnClickListener((View.OnClickListener) v -> {
            Intent intent = new Intent(context, Selected.class);
            intent.putExtra("id", items.stream().map(Items::getId).collect(Collectors.toList()).get(position).toString());
            intent.putExtra("name", items.stream().map(Items::getName).collect(Collectors.toList()).get(position).toString());
            intent.putExtra("isComplete", items.stream().map(Items::isComplete).collect(Collectors.toList()).get(position).toString());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Items> filtered = new ArrayList<>();
            if(constraint.toString().isEmpty())
                filtered.addAll(allItems);
            else {
                for(Items item : allItems){
                    if(Integer.toString(item.id).toLowerCase().contains(constraint.toString().toLowerCase())) filtered.add(item);
                }
            }



            FilterResults results = new FilterResults();
            results.values = filtered;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            for (Items item : ((ArrayList<Items>) results.values)) {
                Log.i("Filter", "publishResults: " + item.toString());
            }
            items.clear();
            items.addAll((ArrayList<Items>) results.values);
            notifyDataSetChanged();
        }
    };

    class Holder extends RecyclerView.ViewHolder {

        TextView txtId, txtName, txtIsComplete;
        RelativeLayout parent;

        public Holder(@NonNull View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.txtId);
            txtName = itemView.findViewById(R.id.txtName);
            txtIsComplete = itemView.findViewById(R.id.txtIsComplete);
            parent = itemView.findViewById(R.id.panelParent);
        }
    }
}
