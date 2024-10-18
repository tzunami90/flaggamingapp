package com.beone.flagggaming.producto;
import android.content.Context;
import android.content.Intent;
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
public class ProductoClienteAdapter extends RecyclerView.Adapter<ProductoClienteAdapter.ProductoClienteViewHolder> {

    private List<Producto> productosList;
    private List<Producto> productosListFull;
    private Context context;
    private OnProductoClickListener onProductoClickListener;

    public ProductoClienteAdapter(Context context, List<Producto> productosList, OnProductoClickListener onProductoClickListener) {
        this.context = context;
        this.productosList = productosList;
        this.productosListFull = new ArrayList<>(productosList); // Copia completa de los datos
        this.onProductoClickListener = onProductoClickListener;
    }

    @NonNull
    @Override
    public ProductoClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoClienteViewHolder(itemView, onProductoClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoClienteViewHolder holder, int position) {
        Producto producto = productosList.get(position);
        holder.bind(producto);
    }

    @Override
    public int getItemCount() {
        return productosList.size();
    }

    class ProductoClienteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewIDProducto;
        private TextView textViewProductDescription;
        private TextView textViewProductPrice;
        private TextView textViewCategoria;
        private TextView textViewTiendaNombre;
        private ImageView imageViewCategoria;
        OnProductoClickListener onProductoClickListener;

        public ProductoClienteViewHolder(@NonNull View itemView, OnProductoClickListener onProductoClickListener) {
            super(itemView);
            textViewIDProducto = itemView.findViewById(R.id.textViewIDProducto);
            textViewProductDescription = itemView.findViewById(R.id.textViewProductDescription);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            textViewCategoria = itemView.findViewById(R.id.textViewCategoria);
            textViewTiendaNombre = itemView.findViewById(R.id.textViewTiendaNombre);
            imageViewCategoria = itemView.findViewById(R.id.imageViewCategoria);
            this.onProductoClickListener = onProductoClickListener;
            itemView.setOnClickListener(this);
        }

        public void bind(Producto producto) {
            textViewIDProducto.setText(producto.getSkuTienda());
            textViewProductDescription.setText(producto.getDescTienda());
            textViewProductPrice.setText("$" + producto.getPrecioVta());
            textViewCategoria.setText(producto.getCategoria().getDesc_categoria());
            textViewTiendaNombre.setText(producto.getTiendaNombre());

            // Cargar la imagen de la categoría desde la URL usando Glide
            Glide.with(itemView.getContext())
                    .load(producto.getCategoria().getImagenUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.nopic)
                    .error(R.drawable.nopic)
                    .into(imageViewCategoria);
        }

        @Override
        public void onClick(View view) {
            onProductoClickListener.onProductoClick(productosList.get(getAdapterPosition()));
        }
    }

    public interface OnProductoClickListener {
        void onProductoClick(Producto producto);
    }

    public void updateList(List<Producto> newList) {
        productosList = newList;
        notifyDataSetChanged();
    }

    public void filterByTextAndCategory(String text, int categoryId) {
        List<Producto> filteredList = new ArrayList<>();
        for (Producto producto : productosListFull) {
            boolean matchesText = producto.getSkuTienda().toLowerCase().contains(text.toLowerCase()) ||
                    producto.getDescTienda().toLowerCase().contains(text.toLowerCase()) ||
                    producto.getMarca().toLowerCase().contains(text.toLowerCase()) ||
                    producto.getCategoria().getDesc_categoria().toLowerCase().contains(text.toLowerCase()) ||
                    producto.getTiendaNombre().toLowerCase().contains(text.toLowerCase());
            boolean matchesCategory = categoryId == 0 || producto.getIdCategoria() == categoryId;

            if (matchesText && matchesCategory) {
                filteredList.add(producto);
            }
        }
        updateList(filteredList);
    }
}