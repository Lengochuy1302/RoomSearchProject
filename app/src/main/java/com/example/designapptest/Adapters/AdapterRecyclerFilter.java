package com.example.designapptest.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.designapptest.ClassOther.myFilter;
import com.example.designapptest.R;

import java.util.List;

public class AdapterRecyclerFilter extends RecyclerView.Adapter<AdapterRecyclerFilter.ViewHolder> {

    Context context;
    int resource;
    List<myFilter> listFilter;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDelete;
        TextView displayName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDelete = itemView.findViewById(R.id.img_delete);
            displayName = itemView.findViewById(R.id.display_name);
        }
    }

    public AdapterRecyclerFilter(Context context,int resource,List<myFilter> listFilter){
        this.context=context;
        this.resource=resource;
        this.listFilter = listFilter;
    }
    @NonNull
    @Override
    public AdapterRecyclerFilter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerFilter.ViewHolder viewHolder, int i) {

        viewHolder.displayName.setText(listFilter.get(i).getName());
        viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listFilter.remove(i);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFilter.size();
    }

}
