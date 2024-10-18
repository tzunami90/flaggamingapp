package com.beone.flagggaming.navbar;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.beone.flagggaming.R;
import com.beone.flagggaming.db.DBHelper;
import com.beone.flagggaming.juegos.CarouselAdapter;
import com.beone.flagggaming.juegos.DetalleJuego;
import com.beone.flagggaming.juegos.Juego;
import com.beone.flagggaming.juegos.HomeJuegoAdapter;
import java.util.Collections;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;
public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewOfertas;
    private RecyclerView recyclerViewDestacados;
    private HomeJuegoAdapter juegoAdapterOfertas;
    private HomeJuegoAdapter juegoAdapterDestacados;
   private ImageView progressLogo;
    private ViewPager2 viewPagerCarrusel;
    private CarouselAdapter carouselAdapter;

    // Variables para el carrusel automático
    private final Handler handler = new Handler();
    private int currentPage = 0;
    private Runnable carruselRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inicializar vistas
        recyclerViewOfertas = view.findViewById(R.id.recyclerViewOfertas);
        recyclerViewDestacados = view.findViewById(R.id.recyclerViewDestacados);
        viewPagerCarrusel = view.findViewById(R.id.viewPagerCarrusel);
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

        // Ejecutar la tarea en segundo plano usando AsyncTask
        new CargarDatosTask(txTitulo, txTitulo2).execute();

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

    // AsyncTask para cargar los datos en segundo plano
    private class CargarDatosTask extends AsyncTask<Void, Void, CargarDatosTask.Resultado> {
        private TextView txTitulo;
        private TextView txTitulo2;

        // Clase para encapsular los resultados
        class Resultado {
            List<Juego> ofertasDestacadas;
            List<Juego> juegosDestacados;
            List<Juego> juegosAleatorios;
        }

        public CargarDatosTask(TextView txTitulo, TextView txTitulo2) {
            this.txTitulo = txTitulo;
            this.txTitulo2 = txTitulo2;
        }

        @Override
        protected Resultado doInBackground(Void... voids) {
            Resultado resultado = new Resultado();

            // Cargar datos desde la base de datos
            resultado.ofertasDestacadas = DBHelper.getOfertasDestacadas(getContext());
            resultado.juegosDestacados = DBHelper.getJuegosDestacados(getContext());
            resultado.juegosAleatorios = DBHelper.getJuegosAleatorios(getContext(), 5);

            // Ordenar juegos destacados por contador de vistas
            Collections.sort(resultado.juegosDestacados, (juego1, juego2) ->
                    Integer.compare(juego2.getContadorVistas(), juego1.getContadorVistas()));

            return resultado;
        }

        @Override
        protected void onPostExecute(Resultado resultado) {
            // Ocultar el logo giratorio y detener la animación
            progressLogo.clearAnimation();
            progressLogo.setVisibility(View.GONE);

            // Mostrar los títulos
            txTitulo.setVisibility(View.VISIBLE);
            txTitulo2.setVisibility(View.VISIBLE);

            // Configurar los adaptadores con los datos cargados
            juegoAdapterOfertas = new HomeJuegoAdapter(getContext(), resultado.ofertasDestacadas, HomeFragment.this::onJuegoClick);
            recyclerViewOfertas.setAdapter(juegoAdapterOfertas);

            juegoAdapterDestacados = new HomeJuegoAdapter(getContext(), resultado.juegosDestacados, HomeFragment.this::onJuegoClick);
            recyclerViewDestacados.setAdapter(juegoAdapterDestacados);

            // Configurar el carrusel
            carouselAdapter = new CarouselAdapter(resultado.juegosAleatorios, HomeFragment.this::onJuegoClick);
            viewPagerCarrusel.setAdapter(carouselAdapter);

            // Iniciar el cambio automático del carrusel
            handler.postDelayed(carruselRunnable, 2000);
        }
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




