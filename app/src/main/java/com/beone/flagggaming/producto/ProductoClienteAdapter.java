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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
public class ProductoClienteAdapter extends RecyclerView.Adapter<ProductoClienteAdapter.ProductoClienteViewHolder> {
    private List<Producto> productosList;
    private Context context;

    public ProductoClienteAdapter(List<Producto> productosList, Context context) {
        this.context = context;
        this.productosList = productosList;
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
            imageViewCategoria.setImageResource(producto.getCategoria().getCategoriaImageResource());
        }

        private int getCategoriaImageResource(int idCategoria) {
            switch (idCategoria) {
                case 1:
                    return R.drawable.cat1;
                case 2:
                    return R.drawable.cat2;
                case 3:
                    return R.drawable.cat3;
                case 4:
                    return R.drawable.cat4;
                case 5:
                    return R.drawable.cat5;
                case 6:
                    return R.drawable.cat6;
                case 7:
                    return R.drawable.cat7;
                case 8:
                    return R.drawable.cat8;
                case 9:
                    return R.drawable.cat9;
                case 10:
                    return R.drawable.cat10;
                case 11:
                    return R.drawable.cat11;
                default:
                    return R.drawable.nopic;
            }
        }
    }
}