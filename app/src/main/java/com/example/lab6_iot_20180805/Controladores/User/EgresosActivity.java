package com.example.lab6_iot_20180805.Controladores.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6_iot_20180805.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class EgresosActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu, perfil;

    LinearLayout lista_super, lista_sitios, nuevo_super, nuevo_sitio, inicio_nav, log_out;

    RecyclerView recyclerView;
    List<engresoDataClass> dataList;
    MyAdapterListaEgresos adapter;
    SearchView searchView;

    Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_egresos);

        initializeDrawer();
        setupRecyclerView();

        FloatingActionButton addSuperButton = findViewById(R.id.floatingButton_addSitio);
        addSuperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Define la nueva Activity que quieres abrir
                Intent intent = new Intent(EgresosActivity.this, user_nuevo_egreso.class);  // Asume que NewSuperActivity es la actividad a la que quieres ir.
                startActivity(intent);
            }
        });
    }

    private void initializeDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu_nav_admin_toolbar);
        menu.setOnClickListener(v -> openDrawer(drawerLayout));
        //--para ir al perfil

        perfil = findViewById(R.id.boton_perfil);

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(EgresosActivity.this, admin_perfil.class);
                startActivity(intent);
            }
        });

        setupDrawerLinks();
    }

    private void setupDrawerLinks() {
        inicio_nav = findViewById(R.id.inicio_nav);
        lista_super = findViewById(R.id.lista_super_nav);
        lista_sitios = findViewById(R.id.lista_sitios_nav);
        nuevo_sitio = findViewById(R.id.nuevo_sitio_nav);
        nuevo_super = findViewById(R.id.nuevo_super_nav);
        log_out = findViewById(R.id.cerrar_sesion);

        inicio_nav.setOnClickListener(v -> redirectActivity(this, AdminActivity.class));
        lista_sitios.setOnClickListener(v -> redirectActivity(this, EgresosActivity.class));
        lista_super.setOnClickListener(v -> redirectActivity(this, IngresosActivity.class));
        nuevo_super.setOnClickListener(v -> redirectActivity(this, user_nuevo_ingreso.class));
        nuevo_sitio.setOnClickListener(v -> redirectActivity(this, user_nuevo_egreso.class));
        log_out.setOnClickListener(v -> Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show());
    }

    private void setupRecyclerView() {
        try {
            recyclerView = findViewById(R.id.recyclerView_listasitios_admin);
            searchView = findViewById(R.id.search_listasitios_admin);
            searchView.clearFocus();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchList(newText);
                    return false;
                }
            });

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(gridLayoutManager);
            dataList = new ArrayList<>();
            adapter = new MyAdapterListaEgresos(this, dataList);
            recyclerView.setAdapter(adapter);

            addTestData();
        } catch (Exception e) {
            Toast.makeText(this, "Error setting up RecyclerView: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("RecyclerViewSetup", "Error setting up RecyclerView", e);
        }
    }

    private void addTestData() {
        try {
            engresoDataClass androidData = new engresoDataClass("Area Telecom1", "5","12345678", "Lima", "Lima", "Lima", 45632L,93871388L, 93841388L, "urbana", "movil" );
            dataList.add(androidData);
            androidData = new engresoDataClass("Area Telecom2", "5","12345678", "Lima", "Lima", "la Victoria", 45632L,93871388L, 93841388L, "urbana", "movil" );
            dataList.add(androidData);
            androidData = new engresoDataClass("Area Telecom3", "5","12345678", "Lima", "Lima", "Surco", 45632L,93871388L, 93841388L, "rural", "movil" );
            dataList.add(androidData);
            androidData = new engresoDataClass("Area Telecom5", "2","12345678", "Lima", "Lima", "La Molina", 45632L,93871388L, 93841388L, "urbana", "movil" );
            dataList.add(androidData);

            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            Toast.makeText(this, "Error adding test data: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("TestDataError", "Error adding test data", e);
        }
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    private void searchList(String text) {
        List<engresoDataClass> dataSearchList = new ArrayList<>();
        for (engresoDataClass data : dataList) {
            if (data.getNombre().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        if (dataSearchList.isEmpty()) {
            Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setSearchList_sitios(dataSearchList);
        }
    }
}
