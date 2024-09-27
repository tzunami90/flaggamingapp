package com.beone.flagggaming.tiendascliente;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beone.flagggaming.R;
import com.beone.flagggaming.tiendas.Tienda;

import java.util.ArrayList;
import java.util.List;

public class TiendaAdapter extends RecyclerView.Adapter<TiendaAdapter.TiendaViewHolder> implements Filterable {

    private List<Tienda> tiendaList;
    private List<Tienda> tiendaListFull;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Tienda tienda);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public TiendaAdapter(List<Tienda> tiendaList) {
        this.tiendaList = tiendaList;
        this.tiendaListFull = new ArrayList<>(tiendaList); // Copy for filtering
    }

    @NonNull
    @Override
    public TiendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tienda_item, parent, false);
        return new TiendaViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TiendaViewHolder holder, int position) {
        Tienda currentTienda = tiendaList.get(position);
        holder.name.setText(currentTienda.getName());
        holder.mail.setText(currentTienda.getMail());
        holder.dir.setText("Dirección: " + currentTienda.getDir());
        holder.days.setText("Dias de Atención: " + currentTienda.getDays());
        holder.hr.setText("Horario de Atención: " + currentTienda.getHr());
        holder.insta.setText("Instagram: " + currentTienda.getInsta());
    }

    @Override
    public int getItemCount() {
        return tiendaList.size();
    }

    @Override
    public Filter getFilter() {
        return tiendaFilter;
    }

    private Filter tiendaFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Tienda> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(tiendaListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Tienda tienda : tiendaListFull) {
                    if (tienda.getName().toLowerCase().contains(filterPattern) ||
                            tienda.getMail().toLowerCase().contains(filterPattern) ||
                            tienda.getDir().toLowerCase().contains(filterPattern) ||
                            tienda.getDays().toLowerCase().contains(filterPattern) ||
                            tienda.getHr().toLowerCase().contains(filterPattern) ||
                            tienda.getInsta().toLowerCase().contains(filterPattern)) {
                        filteredList.add(tienda);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            tiendaList.clear();
            tiendaList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    static class TiendaViewHolder extends RecyclerView.ViewHolder {
        TextView name, mail, dir, days, hr, insta;

        TiendaViewHolder(View itemView, final TiendaAdapter adapter) {
            super(itemView);
            name = itemView.findViewById(R.id.tienda_name);
            mail = itemView.findViewById(R.id.tienda_mail);
            dir = itemView.findViewById(R.id.tienda_dir);
            days = itemView.findViewById(R.id.tienda_days);
            hr = itemView.findViewById(R.id.tienda_hr);
            insta = itemView.findViewById(R.id.tienda_insta);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adapter.listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            adapter.listener.onItemClick(adapter.tiendaList.get(position));
                        }
                    }
                }
            });
        }
    }
}