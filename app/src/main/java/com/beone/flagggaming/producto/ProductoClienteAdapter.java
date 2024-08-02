package com.beone.flagggaming.producto;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beone.flagggaming.R;
import com.beone.flagggaming.producto.Producto;
import com.bumptech.glide.Glide;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
public class ProductoClienteAdapter extends RecyclerView.Adapter<ProductoClienteAdapter.ProductoClienteViewHolder> {
    private List<Producto> productosList;
    private List<Producto> productosListFull;
    private Context context;

    public ProductoClienteAdapter(List<Producto> productosList, Context context) {
        this.context = context;
        this.productosList = productosList;
        this.productosListFull = new ArrayList<>(productosList); // Copia completa de los datos
    }

    @NonNull
    @Override
    public ProductoClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoClienteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoClienteViewHolder holder, int position) {
        Producto producto = productosList.get(position);
        holder.bind(producto);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProductoDetalle.class);
            intent.putExtra("skuTienda", producto.getSkuTienda());
            intent.putExtra("descTienda", producto.getDescTienda());
            intent.putExtra("marca", producto.getMarca());
            intent.putExtra("precioVta", producto.getPrecioVta().toString());
            intent.putExtra("idCategoria", producto.getIdCategoria());
            intent.putExtra("categoriaDesc", producto.getCategoria().getDesc_categoria());
            intent.putExtra("tiendaNombre", producto.getTiendaNombre());
            intent.putExtra("idTienda", producto.getIdTienda());
            intent.putExtra("imagenUrl", producto.getCategoria().getImagenUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productosList.size();
    }

    public static class ProductoClienteViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewIDProducto;
        private TextView textViewProductDescription;
        private TextView textViewProductPrice;
        private TextView textViewCategoria;
        private TextView textViewTiendaNombre;
        private ImageView imageViewCategoria;


        public ProductoClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewIDProducto = itemView.findViewById(R.id.textViewIDProducto);
            textViewProductDescription = itemView.findViewById(R.id.textViewProductDescription);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            textViewCategoria = itemView.findViewById(R.id.textViewCategoria);
            textViewTiendaNombre = itemView.findViewById(R.id.textViewTiendaNombre);
            imageViewCategoria = itemView.findViewById(R.id.imageViewCategoria);
        }

        public void bind(Producto producto) {
            textViewIDProducto.setText(producto.getSkuTienda());
            textViewProductDescription.setText(producto.getDescTienda());
            textViewProductPrice.setText("$" + producto.getPrecioVta());
            textViewCategoria.setText(producto.getCategoria().getDesc_categoria());
            textViewTiendaNombre.setText(producto.getTiendaNombre());

            // Cargar la imagen de la categoría desde la URL usando Glide
            Glide.with(itemView.getContext())
                    .load(producto.getCategoria().getImagenUrl())  // Usa la URL de la imagen
                    .placeholder(R.drawable.nopic)  // Puedes definir un placeholder mientras se carga la imagen
                    .error(R.drawable.nopic)  // Imagen de error si no se puede cargar la URL
                    .into(imageViewCategoria);
        }

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