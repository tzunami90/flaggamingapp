package com.beone.flagggaming;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.beone.flagggaming.epicapi.ListaJuegosEpic;
import com.beone.flagggaming.juegos.ListadoJuegos;
import com.beone.flagggaming.navbar.AboutUsFragment;
import com.beone.flagggaming.navbar.HomeFragment;
import com.beone.flagggaming.navbar.PanelTiendaFragment;
import com.beone.flagggaming.navbar.PerfilUsuarioFragment;
import com.beone.flagggaming.navbar.RegisterTiendaFragment;
import com.beone.flagggaming.producto.ListaHardware;
import com.beone.flagggaming.steamapi.ListaJuegosSteam;
import com.beone.flagggaming.tiendascliente.ListaTiendas;
import com.beone.flagggaming.usuario.LoginActivity;
import com.google.android.material.navigation.NavigationView;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeAcitivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    ImageButton buttonDrawerToggle;
    NavigationView navigationView;
    TextView textUserName, textUserMail;
    String name, mail;
    int idU, rol;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_acitivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        buttonDrawerToggle = findViewById(R.id.buttonDrawerToggle);
        navigationView = findViewById(R.id.nav_view);


        View headerView = navigationView.getHeaderView(0);
        textUserName = headerView.findViewById(R.id.textUserName);
        textUserMail = headerView.findViewById(R.id.textUserMail);

        //Completar con los datos de la BD
        textUserName.setText(name);
        textUserMail.setText(mail);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString("name");
            mail = bundle.getString("mail");
            idU = bundle.getInt("id");
            rol = bundle.getInt("rol");
            updateHeaderView();
        }

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    private void updateHeaderView() {
        if (navigationView != null && name != null && mail != null) {
            navigationView.post(() -> {
                textUserName = navigationView.getHeaderView(0).findViewById(R.id.textUserName);
                textUserMail = navigationView.getHeaderView(0).findViewById(R.id.textUserMail);
                textUserName.setText(name);
                textUserMail.setText(mail);
            });
        }
    }

    private void loadFragment(Fragment fragment) {
        executorService.execute(() -> getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit());
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int itemId = menuItem.getItemId();

        if(itemId == R.id.nav_home) {
            // Llamada a AsyncTask para cargar el fragmento de inicio
            loadFragment(new HomeFragment());
            navigationView.setCheckedItem(R.id.nav_home);
        } else if(itemId == R.id.nav_juegos){
            // Cargar el fragmento ListadoJuegos
            loadFragment(new ListadoJuegos());
            navigationView.setCheckedItem(R.id.nav_juegos);
        } else if(itemId == R.id.nav_steamList) {
            startActivity(new Intent(this, ListaJuegosSteam.class));
        } else if(itemId == R.id.nav_epicList) {
            startActivity(new Intent(this, ListaJuegosEpic.class));
        } else if(itemId == R.id.nav_hardware) {
            startActivity(new Intent(this, ListaHardware.class));
        } else if(itemId == R.id.nav_tiendaslist) {
            startActivity(new Intent(this, ListaTiendas.class));
        } else if((itemId == R.id.nav_registro_tiendas) && (rol == 0)) {
            Bundle bundleR = new Bundle();
            bundleR.putInt("idU",idU);
            RegisterTiendaFragment registerTiendaFragment = new RegisterTiendaFragment();
            registerTiendaFragment.setArguments(bundleR);
            // Llamada a AsyncTask para cargar el fragmento de registro de tiendas
            loadFragment(registerTiendaFragment);
           navigationView.setCheckedItem(R.id.nav_registro_tiendas);
        } else if((itemId == R.id.nav_registro_tiendas) && (rol == 1)) {
            Toast.makeText(this,"YA TIENES UNA TIENDA REGISTRADA", Toast.LENGTH_SHORT).show();
        } else if(itemId == R.id.nav_about_us) {
            // Llamada a AsyncTask para cargar el fragmento "About Us"
            loadFragment(new AboutUsFragment());
           navigationView.setCheckedItem(R.id.nav_about_us);
        } else if((itemId == R.id.nav_panel_tienda) && (rol == 1)) {
            Bundle bundleF = new Bundle();
            bundleF.putInt("id",idU);
            PanelTiendaFragment panelTiendaFragment = new PanelTiendaFragment();
            panelTiendaFragment.setArguments(bundleF);
            // Llamada a AsyncTask para cargar el fragmento del panel de la tienda
            loadFragment(panelTiendaFragment);
            navigationView.setCheckedItem(R.id.nav_panel_tienda);
        } else if((itemId == R.id.nav_panel_tienda) && (rol == 0)) {
            Toast.makeText(this,"NO TIENES UNA TIENDA REGISTRADA", Toast.LENGTH_SHORT).show();
        } else if(itemId == R.id.nav_perfil_usuario) {
            Bundle bundlePU = new Bundle();
            bundlePU.putInt("id",idU);
            PerfilUsuarioFragment perfilUsuarioFragment = new PerfilUsuarioFragment();
            perfilUsuarioFragment.setArguments(bundlePU);
            // Llamada a AsyncTask para cargar el fragmento del perfil del usuario
            loadFragment(perfilUsuarioFragment);
            navigationView.setCheckedItem(R.id.nav_perfil_usuario);
        } else if(itemId == R.id.nav_logout) {
            logout();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void logout() {
        Toast.makeText(this, "Sesión Finalizada", Toast.LENGTH_SHORT).show();
        name = null;
        mail = null;
        idU = 0;
        rol = 0;
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}