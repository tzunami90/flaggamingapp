package com.beone.flagggaming.juegos;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beone.flagggaming.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class HomeJuegoAdapter extends RecyclerView.Adapter<HomeJuegoAdapter.HomeJuegoViewHolder> {

    private Context context;
    private List<Juego> juegosList;
    private OnJuegoClickListener onJuegoClickListener;

    public HomeJuegoAdapter(Context context, List<Juego> juegosList, OnJuegoClickListener onJuegoClickListener) {
        this.context = context;
        this.juegosList = juegosList;
        this.onJuegoClickListener = onJuegoClickListener;
    }

    @NonNull
    @Override
    public HomeJuegoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_juego_oferta, parent, false); // Reutilizando el layout de ofertas
        return new HomeJuegoViewHolder(view, onJuegoClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeJuegoViewHolder holder, int position) {
        Juego juego = juegosList.get(position);
        String imagenUrl = juego.getImagen();

        Log.d("HomeJuegoAdapter", "Binding juego: " + juego.getNombre() + ", Imagen URL: " + imagenUrl);

        Glide.with(context)
                .load(imagenUrl)
                .placeholder(R.drawable.placeholder_image) // placeholder en caso de que no cargue la imagen
                .into(holder.imagenImageView);

        // Mostrar u ocultar el descuento según el tipo de juego (Ofertas o Los Más Buscados)
        if (juego.getDiscount() != null && !juego.getDiscount().isEmpty()) {
            holder.descuentoTextView.setText(juego.getDiscount() + "%");  // Mostrar porcentaje de descuento
            holder.descuentoTextView.setVisibility(View.VISIBLE);
        } else {
            holder.descuentoTextView.setVisibility(View.GONE); // Ocultar si no hay descuento
        }
    }

    @Override
    public int getItemCount() {
        return juegosList.size();
    }

    class HomeJuegoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imagenImageView;
        TextView descuentoTextView; // Para mostrar el descuento
        OnJuegoClickListener onJuegoClickListener;

        public HomeJuegoViewHolder(@NonNull View itemView, OnJuegoClickListener onJuegoClickListener) {
            super(itemView);
            imagenImageView = itemView.findViewById(R.id.imagenImageView);
            descuentoTextView = itemView.findViewById(R.id.descuentoTextView);
            this.onJuegoClickListener = onJuegoClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onJuegoClickListener.onJuegoClick(juegosList.get(getAdapterPosition()).getIdFlagg());
        }
    }

    public interface OnJuegoClickListener {
        void onJuegoClick(String idFlagg);
    }
}
