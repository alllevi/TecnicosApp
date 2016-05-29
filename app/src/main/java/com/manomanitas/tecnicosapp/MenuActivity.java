package com.manomanitas.tecnicosapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    private final String SHARED_PREFS_FILE = "manomanitasConf";
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sharedpreferences = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
    }

    public void onClick_presupuestos(View view) {

        try {
            Intent intent = new Intent(this, PresupuestosActivity.class);
        startActivity(intent);
        } catch (Exception e) {
        }

    }

    public void onClick_comprados(View view) {

        Intent intent = new Intent(this, CompradosActivity.class);
        startActivity(intent);

    }

    public void onClick_miPerfil(View view) {

        Intent intent = new Intent(this, EditarPerfilActivity.class);
        startActivity(intent);

    }

    public void onClick_cerrarSesion(View view) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("ID_TECNICO","-1");
        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);



    }
}
