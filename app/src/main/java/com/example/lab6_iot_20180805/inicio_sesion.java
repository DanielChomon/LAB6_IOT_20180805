package com.example.lab6_iot_20180805;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab6_iot_20180805.Controladores.User.AdminActivity;

import java.util.ArrayList;
import java.util.List;
import android.text.method.PasswordTransformationMethod;

public class inicio_sesion extends AppCompatActivity {

    private EditText correoEditText;
    private EditText contrasenaEditText;
    private Button iniciarSesionButton;

    private CheckBox mostrarContrasenaCheckbox;



    // Método para mostrar u ocultar la contraseña
    private void mostrarOcultarContrasena(boolean mostrar) {
        if (mostrar) {
            // Mostrar contraseña
            contrasenaEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            // Ocultar contraseña
            contrasenaEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        // Mover el cursor al final
        contrasenaEditText.setSelection(contrasenaEditText.getText().length());
    }
}
