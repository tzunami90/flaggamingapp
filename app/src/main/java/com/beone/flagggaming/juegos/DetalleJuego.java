package com.beone.flagggaming.juegos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beone.flagggaming.R;
import com.beone.flagggaming.db.DBHelper;
import com.bumptech.glide.Glide;


public class DetalleJuego extends Fragment {

    private String idFlagg;
    private TextView nombreTextView;
    private TextView descripcionTextView;
    private ImageView imagenImageView;
    private ImageView icSteam, icEpic;
    private TextView precioSteam, precioEpic, precioPareSteam, precioPareEpic, precioPostaSteam, precioPostaEpic;


    public DetalleJuego() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idFlagg = getArguments().getString("idFlagg");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_juego, container, false);

        nombreTextView = view.findViewById(R.id.nombreTextView);
        descripcionTextView = view.findViewById(R.id.descripcionTextView);
        imagenImageView = view.findViewById(R.id.imagenImageView);
        icSteam = view.findViewById(R.id.icSteam);
        icEpic = view.findViewById(R.id.icEpic);
        precioEpic = view.findViewById(R.id.precioEpic);
        precioSteam = view.findViewById(R.id.precioSteam);
        precioPareSteam = view.findViewById(R.id.precioPareSteam);
        precioPareEpic = view.findViewById(R.id.precioPareEpic);
        precioPostaSteam = view.findViewById(R.id.precioPostaSteam);
        precioPostaEpic = view.findViewById(R.id.precioPostaEpic);

        cargarDetallesJuego();

        return view;
    }
    private void cargarDetallesJuego() {
        if (idFlagg != null) {
            new Thread(() -> {
                Juego juego = DBHelper.getJuegoByIdFlagg(getContext(), idFlagg);
                getActivity().runOnUiThread(() -> {
                    if (juego != null) {
                        nombreTextView.setText(juego.getNombre());
                        descripcionTextView.setText(juego.getDescripcionCorta());
                        Glide.with(getContext()).load(juego.getImagen()).into(imagenImageView);
                        // Actualiza otros elementos de UI con los detalles del juego
                    } else {
                        Toast.makeText(getContext(), "Juego no encontrado", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        }
    }
}