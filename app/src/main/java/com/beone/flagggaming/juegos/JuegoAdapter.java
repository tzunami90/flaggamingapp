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
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class JuegoAdapter extends RecyclerView.Adapter<JuegoAdapter.JuegoViewHolder> {

    private Context context;
    private List<Juego> juegosList;
    private List<Juego> juegosListFiltered;
    private OnJuegoClickListener onJuegoClickListener;

    public JuegoAdapter(Context context, List<Juego> juegosList, OnJuegoClickListener onJuegoClickListener) {
        this.context = context;
        this.juegosList = juegosList;
        this.juegosListFiltered = new ArrayList<>(juegosList);
        this.onJuegoClickListener = onJuegoClickListener;
    }

    @NonNull
    @Override
    public JuegoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_juego, parent, false);
        return new JuegoViewHolder(view, onJuegoClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull JuegoViewHolder holder, int position) {
        Juego juego = juegosListFiltered.get(position);
        holder.nombreTextView.setText(juego.getNombre());
        holder.descripcionTextView.setText(juego.getDescripcionCorta());
        // Cargar la imagen del juego en el ImageView usando Glide
        Glide.with(context)
                .load(juego.getImagen())
                .thumbnail(0.1f) // Carga una versión de baja calidad primero
                .placeholder(R.drawable.placeholder_image) // placeholder
                .error(R.drawable.placeholder_image) // imagen de error si falla la carga
                .diskCacheStrategy(DiskCacheStrategy.ALL) // usar caché de disco
                .into(holder.imagenImageView);
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


    class JuegoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nombreTextView;
        TextView descripcionTextView;
        ImageView imagenImageView;
        OnJuegoClickListener onJuegoClickListener;

        public JuegoViewHolder(@NonNull View itemView, OnJuegoClickListener onJuegoClickListener) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            descripcionTextView = itemView.findViewById(R.id.descripcionTextView);
            imagenImageView = itemView.findViewById(R.id.imagenImageView);
            this.onJuegoClickListener = onJuegoClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onJuegoClickListener.onJuegoClick(juegosListFiltered.get(getAdapterPosition()).getIdFlagg());
        }
    }

    public interface OnJuegoClickListener {
        void onJuegoClick(String idFlagg);
    }
}