package com.beone.flagggaming.steamapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beone.flagggaming.R;

import java.util.List;

public class SteamJuegoAdapter extends RecyclerView.Adapter<SteamJuegoAdapter.ViewHolder> {
    private List<Juegos> juegos;
    private List<Response> response;
    private Context context;


    public SteamJuegoAdapter(List<Juegos> juegos, Context context) {
        this.juegos = juegos;
        this.context = context;
    }

    @NonNull
    @Override
    public SteamJuegoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_juegos,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SteamJuegoAdapter.ViewHolder holder, int position) {
        holder.tvAppId.setText(juegos.get(position).getAppid());
        holder.tvName.setText(juegos.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return juegos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAppId, tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAppId=itemView.findViewById(R.id.tvAppId);
            tvName=itemView.findViewById(R.id.tvName);
        }
    }
}
