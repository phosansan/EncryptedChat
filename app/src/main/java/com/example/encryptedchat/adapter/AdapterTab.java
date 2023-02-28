package com.example.encryptedchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.encryptedchat.R;
import com.example.encryptedchat.menu.Chatting;
import com.example.encryptedchat.model.Tab;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class AdapterTab extends RecyclerView.Adapter<AdapterTab.Holder> {
    private List<Tab> list;
    private Context context;

    public AdapterTab(List<Tab> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Tab chat = list.get(position);

        holder.tvNama.setText(chat.getNama());
        holder.tvKet.setText(chat.getKeterangan());
        holder.tvTgl.setText(chat.getTanggal());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, Chatting.class).
                        putExtra("id",chat.getID()).
                        putExtra("nama",chat.getNama()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private TextView tvNama, tvKet, tvTgl;

        public Holder(@NonNull View itemView) {
            super(itemView);

            tvNama = itemView.findViewById(R.id.tv_nama);
            tvKet = itemView.findViewById(R.id.tv_ket);
            tvTgl = itemView.findViewById(R.id.tv_tgl);
        }
    }
}
