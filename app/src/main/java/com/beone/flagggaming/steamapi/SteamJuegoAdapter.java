package com.beone.flagggaming.steamapi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beone.flagggaming.R;

import java.util.List;

public class SteamJuegoAdapter extends RecyclerView.Adapter<SteamJuegoAdapter.ViewHolder> {
    private List<Juegos> appList;
    private List<Response> response;
    private Context context;


    public SteamJuegoAdapter(List<Juegos> appList, Context context) {
        this.appList = appList;
        this.context = context;
    }

    @NonNull
    @Override
    public SteamJuegoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_juegos,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SteamJuegoAdapter.ViewHolder holder, int position) {
        Juegos app = appList.get(position);
        holder.tvAppId.setText(String.valueOf(app.getAppId()));
        holder.tvName.setText(app.getAppName());

        // Almacenar el ID del juego en el ViewHolder
        holder.itemView.setTag(app.getAppId());


        // Agregar onClickListener al CardView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el ID del juego seleccionado del ViewHolder
                int appId = (int) v.getTag();

                // Crear un intent para abrir MostrarJuegoSteam y pasar el ID del juego
                Intent intent = new Intent(context, MostrarJuegoSteam.class);
                intent.putExtra("id", String.valueOf(appId));

                // Agregar el indicador FLAG_ACTIVITY_NEW_TASK al intent
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAppId, tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAppId=itemView.findViewById(R.id.tvAppId);
            tvName=itemView.findViewById(R.id.tvName);
        }
    }
}
