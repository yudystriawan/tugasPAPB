package com.example.yudystriawan.tugaspapb;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter <CustomAdapter.CustomViewHolder> {

    LayoutInflater mInflater;
    ArrayList<User> User;
    private Context context;
    public CustomAdapter(Context context, ArrayList<User> User) {
        this.mInflater = LayoutInflater.from(context);
        this.User = User;
    }

    @NonNull
    @Override
    public CustomAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.row_view, parent, false);
        return new CustomViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomAdapter.CustomViewHolder holder, int position) {
        // Retrieve the data for that position
        final User current = User.get(position);
        // Add the data to the view
        holder.namaItemView.setText(current.nama);
        holder.komentarItemView.setText(current.komentar);

//        holder.linearLayout.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Toast.makeText(context, "You clicked "+current.nama , Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(context,Detail.class);
//                intent.putExtra("LINK",current.link);
//                intent.putExtra("IMG", current.logo);
//                context.startActivity(intent);
//            }
//        });
        // add the Listener to the view of that position if desired
//        holder.logoItemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), current.nama+" removed", Toast.LENGTH_LONG).show();
//                Sisop.remove(current);
//                notifyDataSetChanged();
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return User.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private final CustomAdapter mAdapter;
        public TextView namaItemView;
        public TextView komentarItemView;


        public CustomViewHolder(@NonNull View itemView, CustomAdapter adapter) {
            super(itemView);
            namaItemView = itemView.findViewById(R.id.nama);
            komentarItemView = itemView.findViewById(R.id.komentar);
            context = itemView.getContext();
            this.mAdapter = adapter;

        }
    }
}