package com.beone.flagggaming.juegos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beone.flagggaming.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {

    private List<Juego> juegosList;
    private OnJuegoClickListener onJuegoClickListener;

    public CarouselAdapter(List<Juego> juegosList, OnJuegoClickListener onJuegoClickListener) {
        this.juegosList = juegosList;
        this.onJuegoClickListener = onJuegoClickListener;
    }

    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrusel_juego, parent, false);
        return new CarouselViewHolder(view, onJuegoClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        Juego juego = juegosList.get(position);
        String imagenUrl = juego.getImagen();

        // Cargar la imagen del juego en el ImageView usando Glide o similar
        Glide.with(holder.itemView.getContext())
                .load(imagenUrl)
                .thumbnail(0.1f) // Carga una versión de baja calidad primero
                .placeholder(R.drawable.placeholder_image) // placeholder
                .error(R.drawable.placeholder_image) // imagen de error si falla la carga
                .diskCacheStrategy(DiskCacheStrategy.ALL) // usar caché de disco
                .into(holder.imagenJuego);
    }

    @Override
    public int getItemCount() {
        return juegosList.size();
    }

    class CarouselViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imagenJuego;
        OnJuegoClickListener onJuegoClickListener;

        public CarouselViewHolder(@NonNull View itemView, OnJuegoClickListener onJuegoClickListener) {
            super(itemView);
            imagenJuego = itemView.findViewById(R.id.imagenJuego);
            this.onJuegoClickListener = onJuegoClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onJuegoClickListener.onJuegoClick(juegosList.get(getAdapterPosition()).getIdFlagg());
        }
    }

    public interface OnJuegoClickListener {
        void onJuegoClick(String idFlagg);
    }
}
