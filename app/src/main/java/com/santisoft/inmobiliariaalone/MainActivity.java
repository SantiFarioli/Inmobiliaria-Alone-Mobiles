package com.santisoft.inmobiliariaalone;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.santisoft.inmobiliariaalone.data.local.SessionManager;
import com.santisoft.inmobiliariaalone.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Configuración de navegación
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio,
                R.id.nav_perfil,
                R.id.nav_inmuebles,
                R.id.nav_inquilinos,
                R.id.nav_contratos,
                R.id.nav_logout
        )
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Mostrar nombre completo y email del propietario al iniciar
        refrescarHeader();
    }

    // Método reutilizable para refrescar el header dinámicamente
    public void refrescarHeader() {
        SessionManager session = new SessionManager(this);
        View headerView = binding.navView.getHeaderView(0);

        TextView tvNombre = headerView.findViewById(R.id.tvNombreCompleto);
        TextView tvEmail = headerView.findViewById(R.id.tvEmailPropietario);

        String nombre = session.getNombre();
        String apellido = session.getApellido();
        String nombreCompleto = (nombre + " " + (apellido != null ? apellido : "")).trim();

        tvNombre.setText(nombreCompleto.isEmpty() ? "Propietario" : nombreCompleto);
        tvEmail.setText(session.getEmail() != null ? session.getEmail() : "usuario@email.com");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
