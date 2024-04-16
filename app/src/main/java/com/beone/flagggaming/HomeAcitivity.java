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

import com.beone.flagggaming.navbar.AboutUsFragment;
import com.beone.flagggaming.navbar.HomeFragment;
import com.beone.flagggaming.navbar.PanelTiendaFragment;
import com.beone.flagggaming.navbar.RegisterTiendaFragment;
import com.beone.flagggaming.usuario.LoginActivity;
import com.google.android.material.navigation.NavigationView;

public class HomeAcitivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    ImageButton buttonDrawerToggle;
    NavigationView navigationView;
    TextView textUserName, textUserMail;
    Bundle bundle;
    String name, mail;
    int idU, rol;

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

        bundle = getIntent().getExtras();
        name = bundle.getString("name");
        mail = bundle.getString("mail");
        idU = bundle.getInt("id");
        rol = bundle.getInt("rol");

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

        //Ingreso al home fragment
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int itemId = menuItem.getItemId();

        if(itemId == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        if((itemId == R.id.nav_registro_tiendas) && (rol == 0)) {
            Bundle bundleR = new Bundle();
            bundleR.putInt("idU",idU);
            RegisterTiendaFragment registerTiendaFragment = new RegisterTiendaFragment();
            registerTiendaFragment.setArguments(bundleR);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,  registerTiendaFragment).commit();
            navigationView.setCheckedItem(R.id.nav_registro_tiendas);
        }
        if((itemId == R.id.nav_registro_tiendas) && (rol == 1)) {
            Toast.makeText(this,"YA TIENES UNA TIENDA REGISTRADA", Toast.LENGTH_SHORT).show();
        }
        if(itemId == R.id.nav_about_us) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutUsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_about_us);
        }
        if((itemId == R.id.nav_panel_tienda) && (rol == 1)) {
            Bundle bundleF = new Bundle();
            bundleF.putInt("id",idU);
            PanelTiendaFragment panelTiendaFragment = new PanelTiendaFragment();
            panelTiendaFragment.setArguments(bundleF);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, panelTiendaFragment).commit();
            navigationView.setCheckedItem(R.id.nav_panel_tienda);
        }
        if((itemId == R.id.nav_panel_tienda) && (rol == 0)) {
            Toast.makeText(this,"NO TIENES UNA TIENDA REGISTRADA", Toast.LENGTH_SHORT).show();
        }
        if(itemId == R.id.nav_logout) {
            Toast.makeText(this,"Sesión Finalizada", Toast.LENGTH_SHORT).show();
            name = null;
            mail = null;
            idU = 0;
            rol = 0;
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        }
        drawerLayout.close();
        return false;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    public void openNavBar(View v){
        drawerLayout.open();
    }
}