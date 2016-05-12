package com.manomanitas.tecnicosapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void onClick_presupuestos(View view) {

        try {
            Intent intent = new Intent(this, PresupuestosActivity.class);
        startActivity(intent);
        } catch (Exception e) {
        }

    }

    public void onClick_comprados(View view) {

        //Intent intent = new Intent(this, ItemListActivity.class);
       // startActivity(intent);

    }

    public void onClick_miPerfil(View view) {

        Intent intent = new Intent(this, EditarPerfilActivity.class);
        startActivity(intent);

    }
}
