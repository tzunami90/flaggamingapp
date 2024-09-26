package com.beone.flagggaming.navbar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beone.flagggaming.R;
import com.beone.flagggaming.db.DBHelper;
import com.beone.flagggaming.juegos.CarouselAdapter;
import com.beone.flagggaming.juegos.DetalleJuego;
import com.beone.flagggaming.juegos.Juego;
import com.beone.flagggaming.juegos.HomeJuegoAdapter; // Asegúrate de importar el nuevo adaptador
import com.beone.flagggaming.juegos.JuegoAdapter;
import java.util.Collections;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;
public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewOfertas;
    private RecyclerView recyclerViewDestacados;
    private HomeJuegoAdapter juegoAdapterOfertas;
    private HomeJuegoAdapter juegoAdapterDestacados;
   // private ProgressBar progressBar;
   private ImageView progressLogo;
    private ViewPager2 viewPagerCarrusel;
    private CarouselAdapter carouselAdapter;

    // Variables para el carrusel automático
    private Handler handler = new Handler();
    private int currentPage = 0;
    private Runnable carruselRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inicializar vistas
        recyclerViewOfertas = view.findViewById(R.id.recyclerViewOfertas);
        recyclerViewDestacados = view.findViewById(R.id.recyclerViewDestacados);
        viewPagerCarrusel = view.findViewById(R.id.viewPagerCarrusel);
        //progressBar = view.findViewById(R.id.progressBar);
        progressLogo = view.findViewById(R.id.progressLogo);

        // Referencias a los títulos de "Ofertas" y "Los más buscados"
        TextView txTitulo = view.findViewById(R.id.txTitulo);
        TextView txTitulo2 = view.findViewById(R.id.txTitulo2);

        // Configurar los RecyclerViews con GridLayoutManager para mostrar 2 columnas
        recyclerViewOfertas.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewDestacados.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Mostrar el logo y ejecutar la animación
        Animation rotation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_animation);
        progressLogo.startAnimation(rotation);
        progressLogo.setVisibility(View.VISIBLE);
        new Thread(() -> cargarDatos(txTitulo, txTitulo2)).start(); // Pasamos los TextViews a cargarDatos

        // Configurar el cambio automático del carrusel
        carruselRunnable = new Runnable() {
            @Override
            public void run() {
                // Número total de páginas en el carrusel
                int totalPages = carouselAdapter.getItemCount();

                // Cambiar la página actual
                if (currentPage == totalPages) {
                    currentPage = 0;
                }

                viewPagerCarrusel.setCurrentItem(currentPage++, true);

                // Volver a ejecutar el Runnable después de 2 segundos
                handler.postDelayed(this, 2000);
            }
        };

        return view;
    }

    private void cargarDatos(TextView txTitulo, TextView txTitulo2) {
        // Cargar los juegos desde la base de datos
        List<Juego> ofertasDestacadas = DBHelper.getOfertasDestacadas(getContext());
        List<Juego> juegosDestacados = DBHelper.getJuegosDestacados(getContext());

        // Cargar juegos aleatorios para el carrusel (dejamos 5 por ejemplo)
        List<Juego> juegosAleatorios = DBHelper.getJuegosAleatorios(getContext(), 5);

        // Ordenar los juegos destacados por contadorVistas
        Collections.sort(juegosDestacados, (juego1, juego2) -> Integer.compare(juego2.getContadorVistas(), juego1.getContadorVistas()));

        // Volver al hilo principal para actualizar la UI
        getActivity().runOnUiThread(() -> {
            // Ocultar el logo giratorio
            progressLogo.clearAnimation();  // Detener la animación
            progressLogo.setVisibility(View.GONE);

            // Hacer visibles los títulos una vez cargados los datos
            txTitulo.setVisibility(View.VISIBLE);
            txTitulo2.setVisibility(View.VISIBLE);

            // Actualizar los adaptadores con los datos cargados
            juegoAdapterOfertas = new HomeJuegoAdapter(getContext(), ofertasDestacadas, this::onJuegoClick);
            recyclerViewOfertas.setAdapter(juegoAdapterOfertas);

            juegoAdapterDestacados = new HomeJuegoAdapter(getContext(), juegosDestacados, this::onJuegoClick);
            recyclerViewDestacados.setAdapter(juegoAdapterDestacados);

            // Configurar el carrusel
            carouselAdapter = new CarouselAdapter(juegosAleatorios, this::onJuegoClick);
            viewPagerCarrusel.setAdapter(carouselAdapter);

            // Iniciar el cambio automático del carrusel
            handler.postDelayed(carruselRunnable, 2000);  // Inicia después de 2 segundos

        });
    }

    // Manejar el clic en un juego
    public void onJuegoClick(String idFlagg) {
        new Thread(() -> DBHelper.incrementarContadorVistas(getContext(), idFlagg)).start();

        Bundle bundle = new Bundle();
        bundle.putString("idFlagg", idFlagg);

        DetalleJuego detalleJuegoFragment = new DetalleJuego();
        detalleJuegoFragment.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, detalleJuegoFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Eliminar el Runnable cuando la vista se destruya para evitar fugas de memoria
        handler.removeCallbacks(carruselRunnable);
    }
}




