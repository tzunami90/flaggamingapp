package com.beone.flagggaming.navbar;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beone.flagggaming.R;
import com.beone.flagggaming.db.DBHelper;
import com.beone.flagggaming.tiendas.EliminarTiendaFragment;
import com.beone.flagggaming.tiendas.MisProductosFragment;
import com.beone.flagggaming.tiendas.NewProductFragment;
import com.beone.flagggaming.tiendas.PerfilTiendaFragment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PanelTiendaFragment extends Fragment {
    int idU,idT;
    TextView txNroTienda, txNombreTienda;
    CardView cvNuevoProducto, cvPerfilTienda, cvMisProductos, cvEliminarTienda;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idU = getArguments().getInt("id");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_panel_tienda, container, false);

        txNroTienda = root.findViewById(R.id.txNroTienda);
        txNombreTienda = root.findViewById(R.id.txNombreTienda);
        cvNuevoProducto = root.findViewById(R.id.cvNuevoProducto);
        cvPerfilTienda = root.findViewById(R.id.cvPerfilTienda);
        cvMisProductos = root.findViewById(R.id.cvMisProductos);
        cvEliminarTienda = root.findViewById(R.id.cvEliminarTIenda);
        progressBar = root.findViewById(R.id.progressBar);


        // Iniciar tarea asincrónica para obtener los datos de la tienda
        new ObtenerDatosTiendaTask().execute();

        // Habilitar clics en los CardView
        cvNuevoProducto.setOnClickListener(v -> goNuevoProducto());
        cvPerfilTienda.setOnClickListener(v -> goPerfilTienda());
        cvMisProductos.setOnClickListener(v -> goMisProductos());
        cvEliminarTienda.setOnClickListener(v -> goEliminarTienda());

        return root;
    }
    // Tarea asincrónica para obtener los datos de la tienda
    private class ObtenerDatosTiendaTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // Antes de ejecutar la tarea, muestra el ProgressBar
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try (Connection conexion = DBHelper.conDB(getContext())) {
                if (conexion != null) {
                    PreparedStatement pstT = conexion.prepareStatement("SELECT * FROM usuarios_tiendas WHERE idU = ?");
                    pstT.setInt(1, idU);
                    ResultSet rsT = pstT.executeQuery();
                    if (rsT.next()) {
                        idT = rsT.getInt(2);
                        // Actualizar el UI en el hilo principal
                        if (isAdded()) {
                            // El fragmento está adjunto a una actividad, es seguro llamar a getActivity()
                            getActivity().runOnUiThread(() -> txNroTienda.setText("Tienda Nro.: " + idT));
                        }
                    }

                    PreparedStatement pst2 = conexion.prepareStatement("SELECT * FROM tiendas WHERE id = ?");
                    pst2.setInt(1, idT);
                    ResultSet rs2 = pst2.executeQuery();
                    if (rs2.next()) {
                        String nombreTienda = rs2.getString(4);
                        // Actualizar el UI en el hilo principal
                        if (isAdded()) {
                            // El fragmento está adjunto a una actividad, es seguro llamar a getActivity()
                            getActivity().runOnUiThread(() -> txNombreTienda.setText(nombreTienda));
                        }
                    }
                } else {
                    // Mostrar mensaje de error en el hilo principal
                    if (isAdded()) {
                        // El fragmento está adjunto a una actividad, es seguro llamar a getActivity()
                        getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "ERROR DE CONEXION - Por favor reintente en unos momentos", Toast.LENGTH_SHORT).show());
                    }
                }
            } catch (SQLException e) {
                // Mostrar mensaje de error en el hilo principal
                if (isAdded()) {
                    // El fragmento está adjunto a una actividad, es seguro llamar a getActivity()
                    getActivity().runOnUiThread(() -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Después de que la tarea haya finalizado, oculta el ProgressBar
            progressBar.setVisibility(View.GONE);
        }
    }
    //Métodos de enlace cardviews
    public void goNuevoProducto(){
        Bundle bundleNP = new Bundle();
        bundleNP.putInt("idU",idU);
        bundleNP.putInt("idT",idT);
        NewProductFragment newProductFragment = new NewProductFragment();
        newProductFragment.setArguments(bundleNP);
        if (isAdded()) {
            // El fragmento está adjunto a una actividad, es seguro llamar a getActivity()
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newProductFragment).addToBackStack(null).commit();
        }
    }
    public void goPerfilTienda(){
        Bundle bundlePT = new Bundle();
        bundlePT.putInt("idU",idU);
        bundlePT.putInt("idT",idT);
        PerfilTiendaFragment perfilTiendaFragment = new PerfilTiendaFragment();
        perfilTiendaFragment.setArguments(bundlePT);
        if (isAdded()) {
            // El fragmento está adjunto a una actividad, es seguro llamar a getActivity()
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, perfilTiendaFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
    public void goMisProductos(){
        Bundle bundleMP = new Bundle();
        bundleMP.putInt("idU",idU);
        bundleMP.putInt("idT",idT);
        MisProductosFragment misProductosFragment = new MisProductosFragment();
        misProductosFragment.setArguments(bundleMP);
        if (isAdded()) {
            // El fragmento está adjunto a una actividad, es seguro llamar a getActivity()
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, misProductosFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
    public void goEliminarTienda(){
        if (isAdded()) {
            // El fragmento está adjunto a una actividad, es seguro llamar a getActivity()
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new EliminarTiendaFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }
}