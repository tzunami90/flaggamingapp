package com.beone.flagggaming.producto;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beone.flagggaming.R;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {
    private Context context;
    private List<Producto> productosList;
    private OnProductoClickListener onProductoClickListener;

    public ProductoAdapter(Context context, List<Producto> productosList, OnProductoClickListener onProductoClickListener) {
        this.context = context;
        this.productosList = productosList;
        this.onProductoClickListener = onProductoClickListener;
    }
    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_productos, parent, false);
        return new ProductoViewHolder(view, onProductoClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productosList.get(position);
        holder.textViewIDProducto.setText(String.valueOf(producto.getSkuTienda()));
        holder.textViewProductDescription.setText(producto.getDescTienda());
        holder.textViewProductPrice.setText("$" + producto.getPrecioVta());
        holder.textViewCategoria.setText(producto.getCategoria().getDesc_categoria());
    }

    @Override
    public int getItemCount() {
        return productosList.size();
    }

    class ProductoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewIDProducto;
        TextView textViewProductDescription;
        TextView textViewProductPrice;
        TextView textViewCategoria;
        OnProductoClickListener onProductoClickListener;

        public ProductoViewHolder(@NonNull View itemView, OnProductoClickListener onProductoClickListener) {
            super(itemView);
            textViewIDProducto = itemView.findViewById(R.id.textViewIDProducto);
            textViewProductDescription = itemView.findViewById(R.id.textViewProductDescription);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            textViewCategoria = itemView.findViewById(R.id.textViewCategoria);
            this.onProductoClickListener = onProductoClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onProductoClickListener.onProductoClick(productosList.get(getAdapterPosition()).getIdInternoProducto());
        }
    }

    public interface OnProductoClickListener {
        void onProductoClick(int productId);
    }
}