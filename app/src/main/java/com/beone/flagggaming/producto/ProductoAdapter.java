package com.beone.flagggaming.producto;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beone.flagggaming.R;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {
    private List<Producto> productosList;
    private ProductoAdapter.OnItemClickListener mListener; // Define el listener
    public ProductoAdapter(List<Producto> productosList) {
        this.productosList = productosList;
    }
    public interface OnItemClickListener {
        void onItemClick(int productId);
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productos, parent, false);
        return new ProductoViewHolder(itemView);
    }
    public void setOnItemClickListener(ProductoAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productosList.get(position);
        holder.bind(producto);
        // Agregar el listener de clic al item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition();
                if (mListener != null && clickedPosition != RecyclerView.NO_POSITION) {
                    int productId = productosList.get(clickedPosition).getIdInternoProducto(); // Obtener el ID del producto
                    mListener.onItemClick(productId); // Pasar el ID del producto al listener
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productosList.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewIDProducto;
        private TextView textViewProductDescription;
        private TextView textViewProductPrice;
        private TextView textViewCategoria;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewIDProducto = itemView.findViewById(R.id.textViewIDProducto);
            textViewProductDescription = itemView.findViewById(R.id.textViewProductDescription);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            textViewCategoria = itemView.findViewById(R.id.textViewCategoria);

        }

        public void bind(Producto producto) {
            textViewIDProducto.setText(String.valueOf(producto.getSkuTienda())); // Convertir el ID de tienda a String
            textViewProductDescription.setText(producto.getDescTienda());
            textViewProductPrice.setText("$" + producto.getPrecioVta());
            textViewCategoria.setText(producto.getCategoria().getDesc_categoria());
        }

    }
}