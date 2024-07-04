package com.example.lab6_iot_20180805.Controladores.User;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.lab6_iot_20180805.LoginActivity;
import com.example.lab6_iot_20180805.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class user_nuevo_ingreso extends AppCompatActivity {


    //----UPLOAD -----

    FirebaseFirestore db;
    ListenerRegistration snapshotListener;
    FirebaseUser currentUser;


    ImageView foto_perfil;

    Button boton_guardar_nuevoSuper;

    String canal1 = "importanteDefault";

    EditText nuevo_nombre, nuevo_apellido, nuevo_telefono, nuevo_direccion,nuevo_dni,nuevo_correo, nuevo_pass_superad;

    String imageUrl;

    Uri uri;


    //----------------------------------

    DrawerLayout drawerLayout;
    ImageView menu, perfil;

    LinearLayout lista_super, lista_sitios, nuevo_super, nuevo_sitio, inicio_nav, log_out;

    private TextView textViewBienvenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_egreso);

        //-----------NOTIFICACIONES---------------

        crearCanalesNot();



        //----------------------------------------



        //DRAWER------------------------------------

        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu_nav_admin_toolbar);
        inicio_nav = findViewById(R.id.inicio_nav);
        lista_super = findViewById(R.id.lista_super_nav);
        lista_sitios = findViewById(R.id.lista_sitios_nav);
        nuevo_sitio = findViewById(R.id.nuevo_sitio_nav);
        nuevo_super = findViewById(R.id.nuevo_super_nav);
        log_out = findViewById(R.id.cerrar_sesion);

        // correo para hallar el usuario (admin)

        String correo_usuario = getIntent().getStringExtra("correo");


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });

        inicio_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(user_nuevo_ingreso.this, AdminActivity.class);
            }
        });

        lista_sitios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(user_nuevo_ingreso.this, EgresosActivity.class);
            }
        });

        lista_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(user_nuevo_ingreso.this, IngresosActivity.class);
            }
        });

        nuevo_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();            }
        });
        nuevo_sitio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(user_nuevo_ingreso.this, user_nuevo_egreso.class);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cerrar sesión y redirigir a MainActivity
                Intent intent = new Intent(user_nuevo_ingreso.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        //-----------------------------------

        // UPLOAD

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        nuevo_nombre = findViewById(R.id.nombre_nuevoSuper);
        nuevo_apellido = findViewById(R.id.apellido_nuevoSuper);
        nuevo_dni = findViewById(R.id.DNI_nuevoSuper);
        nuevo_direccion = findViewById(R.id.direccion_nuevoSuper);
        nuevo_correo = findViewById(R.id.correo_nuevoSuper);
        nuevo_telefono = findViewById(R.id.telefono_nuevoSuper);
        foto_perfil = findViewById(R.id.subir_foto_super);
        nuevo_pass_superad = findViewById(R.id.pass_superad_nuevoSuper);
        boton_guardar_nuevoSuper = findViewById(R.id.botno_guardar);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            foto_perfil.setImageURI(uri);
                        }else {
                            Toast.makeText(user_nuevo_ingreso.this, "No image selected", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        );

        foto_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photopicker = new Intent(Intent.ACTION_PICK);
                photopicker.setType("image/*");
                activityResultLauncher.launch(photopicker);
            }
        });

        boton_guardar_nuevoSuper.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                saveData();

            }


        });

    }

    public void saveData(){

        notificarImportanceDefault();


        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Usuario_imagen").child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(user_nuevo_ingreso.this);
        builder.setCancelable(false);
        builder.setView(R.layout.admin_progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();
                uploadData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }

    public  void uploadData( ){
        String nombre = nuevo_nombre.getText().toString();
        String apellido = nuevo_apellido.getText().toString();
        String correo = nuevo_correo.getText().toString();
        String telefono = nuevo_telefono.getText().toString();
        String direccion = nuevo_direccion.getText().toString();
        String dni = nuevo_dni.getText().toString();

        String key_dni = nuevo_dni.getText().toString();
        String uid = currentUser.getUid();
        String correo_superad = currentUser.getEmail();
        String pass_superad = nuevo_pass_superad.getText().toString();



        Usuario usuario = new Usuario(nombre, apellido, dni,correo, pass_superad, direccion, "supervisor1", "activo", imageUrl, telefono ,uid, correo_superad, pass_superad, "1");

        if(currentUser != null){

            db.collection("usuarios_por_auth")
                    .document(uid)
                    .collection("usuarios")
                    .add(usuario)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(user_nuevo_ingreso.this, "Saved", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->{
                        Toast.makeText(user_nuevo_ingreso.this, "Algo paso al guardar", Toast.LENGTH_SHORT).show();

                    });
        }else {
            Toast.makeText(user_nuevo_ingreso.this, "No esta logueado", Toast.LENGTH_SHORT).show();

        }

        /*FirebaseDatabase.getInstance().getReference("usuarios").child(key_dni)
                .setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(admin_nuevoSuperActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(admin_nuevoSuperActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }); */



    }



    public  static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static  void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    //-------------------------

    public void crearCanalesNot(){
        NotificationChannel channel = new NotificationChannel(canal1,
                "Canal notificaciones default",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Canal para notificaciones con rpioridad default");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        pedirPermiso();

    }

    public  void pedirPermiso(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(user_nuevo_ingreso.this,
                    new String[]{POST_NOTIFICATIONS}, 101);

        }
    }

    public void notificarImportanceDefault(){
        Intent intent = new Intent(this, IngresosActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, canal1)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Nuevo Supervisor Guardado")
                .setContentText("Ha sido creado un nuevo supervisor")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        Notification notification = builder.build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if(ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS)==PackageManager.PERMISSION_GRANTED){

            notificationManager.notify(1, notification);

        }
    }
}
