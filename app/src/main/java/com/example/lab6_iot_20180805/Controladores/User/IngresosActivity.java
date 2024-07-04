package com.example.lab6_iot_20180805.Controladores.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6_iot_20180805.R;
import com.example.lab6_iot_20180805.entity.Ingreso;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class IngresosActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu, perfil;

    LinearLayout lista_super, lista_sitios, nuevo_super, nuevo_sitio, inicio_nav, log_out;

    RecyclerView recyclerView;
// para FIREBASE----------------

    FirebaseFirestore db;
    FirebaseUser currentUser;
    //----------
    List<Ingreso> dataList;

    DatabaseReference databaseReference;

    ValueEventListener eventListener;

//----------------------------------
    //List<admin_DataClass> dataList;
    MyAdapterListaIngresos adapter;
    SearchView searchView;

    Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_ingresos);

        // correo para hallar el usuario (admin)

        String correo_usuario = getIntent().getStringExtra("correo");

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        initializeDrawer();
        recyclerView = findViewById(R.id.recyclerView_listasuper_admin);
        searchView = findViewById(R.id.search_listasuper_admin);
        searchView.clearFocus();


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        // firebase----------------

        AlertDialog.Builder builder = new AlertDialog.Builder(IngresosActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.admin_progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();
        adapter = new MyAdapterListaIngresos(this, dataList);
        recyclerView.setAdapter(adapter);

        //Firebase-----

        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        dialog.show();

        //------------------------------------- FIRESTORE


        String uid = currentUser.getUid();

        db.collection("usuarios_por_auth")
                .document(uid)
                .collection("usuarios")
                .whereEqualTo("rol", "supervisor1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            dataList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               Ingreso ingreso = document.toObject(Ingreso.class);
                                dataList.add(ingreso);
                            }
                            adapter.notifyDataSetChanged();

                            dialog.dismiss();

                        } else {
                            // Manejar la situación cuando la consulta falla
                            Log.d("Firestore", "Error getting documents: ", task.getException());
                        }
                    }
                });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchList(newText);

                return true;
            }
        });

        //---------------------------

        FloatingActionButton addSuperButton = findViewById(R.id.floatingButton_addSuper);
        addSuperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Define la nueva Activity que quieres abrir
                Intent intent = new Intent(IngresosActivity.this, user_nuevo_ingreso.class);  // Asume que NewSuperActivity es la actividad a la que quieres ir.
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

                Intent intent  = new Intent(IngresosActivity.this, admin_perfil.class);
                startActivity(intent);
            }
        });

        //-------

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
        lista_super.setOnClickListener(v -> redirectActivity(this, EgresosActivity.class));
        nuevo_super.setOnClickListener(v -> redirectActivity(this, user_nuevo_ingreso.class));
        nuevo_sitio.setOnClickListener(v -> redirectActivity(this, user_nuevo_egreso.class));
        log_out.setOnClickListener(v -> Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show());
    }

    private void setupRecyclerView() {
        try {
            recyclerView = findViewById(R.id.recyclerView_listasuper_admin);
            searchView = findViewById(R.id.search_listasuper_admin);
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
            adapter = new MyAdapterListaIngresos(this, dataList);
            recyclerView.setAdapter(adapter);

            //Firebase-----

            databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");


            //-------------

        } catch (Exception e) {
            Toast.makeText(this, "Error setting up RecyclerView: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("RecyclerViewSetup", "Error setting up RecyclerView", e);
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
        ArrayList<Usuario> dataSearchList = new ArrayList<>();
        for (Usuario data : dataList) {
            if (data.getNombre().toLowerCase().contains(text.toLowerCase()) || data.getApellido().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        if (dataSearchList.isEmpty()) {
            Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setSearchList(dataSearchList);
        }
    }
}
