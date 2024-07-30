package com.beone.flagggaming.juegos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beone.flagggaming.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class JuegoAdapter extends RecyclerView.Adapter<JuegoAdapter.JuegoViewHolder> {

    private Context context;
    private List<Juego> juegosList;
    private List<Juego> juegosListFiltered;

    public JuegoAdapter(Context context, List<Juego> juegosList) {
        this.context = context;
        this.juegosList = juegosList;
        this.juegosListFiltered = new ArrayList<>(juegosList);
    }

    @NonNull
    @Override
    public JuegoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_juego, parent, false);
        return new JuegoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JuegoViewHolder holder, int position) {
        Juego juego = juegosList.get(position);
        holder.nombreTextView.setText(juego.getNombre());
        holder.descripcionTextView.setText(juego.getDescripcionCorta());
        Glide.with(context).load(juego.getImagen()).into(holder.imagenImageView);
    }

    @Override
    public int getItemCount() {
        return juegosListFiltered.size();
    }

    public void filter(String query) {
        query = query.toLowerCase();
        juegosListFiltered.clear();
        if (query.isEmpty()) {
            juegosListFiltered.addAll(juegosList);
        } else {
            for (Juego juego : juegosList) {
                if (juego.getNombre().toLowerCase().contains(query) ||
                        juego.getDescripcionCorta().toLowerCase().contains(query)) {
                    juegosListFiltered.add(juego);
                }
            }
        }
        notifyDataSetChanged();
    }

    class JuegoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView descripcionTextView;
        ImageView imagenImageView;

        public JuegoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            descripcionTextView = itemView.findViewById(R.id.descripcionTextView);
            imagenImageView = itemView.findViewById(R.id.imagenImageView);
        }
    }
}