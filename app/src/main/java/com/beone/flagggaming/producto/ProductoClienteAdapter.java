package com.beone.flagggaming.producto;
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
    private OnItemClickListener mListener;

    public ProductoClienteAdapter(List<Producto> productosList) {
        this.productosList = productosList;
    }

    public interface OnItemClickListener {
        void onItemClick(int productId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition();
                if (mListener != null && clickedPosition != RecyclerView.NO_POSITION) {
                    int productId = productosList.get(clickedPosition).getIdInternoProducto();
                    mListener.onItemClick(productId);
                }
            }
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
    }
}