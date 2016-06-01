package com.manomanitas.tecnicosapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    private final String SHARED_PREFS_FILE = "manomanitasConf";
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Obtenemos objeto sharedPreferences
        sharedpreferences = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);

    }

    public void onClick_presupuestos(View view) {

        try {
            Intent intent = new Intent(this, PresupuestosActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "No se ha podido acceder a presupuestos", Toast.LENGTH_SHORT).show();
            e.printStackTrace();}

    }

    public void onClick_comprados(View view) {

        try {
            Intent intent = new Intent(this, CompradosActivity.class);
            startActivity(intent);
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(), "No se ha podido acceder a presupuestos comprados", Toast.LENGTH_SHORT).show();
            e.printStackTrace();}


    }

    public void onClick_miPerfil(View view) {

        try {
            Intent intent = new Intent(this, EditarPerfilActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "No se ha podido acceder a editar perfil", Toast.LENGTH_SHORT).show();
            e.printStackTrace();}


    }

    public void onClick_cerrarSesion(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);

        builder.setMessage("Dejará de recibir notificaciones de avisos")
                .setTitle("¿Quiere cerrar su sesión?");

        builder.setPositiveButton("Cerrar sesión", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Cerramos la sesion
                cerrarSesion();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //No hacemos nada
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void cerrarSesion(){
        try {
            //Establecemos el id del tecnico a -1
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("ID_TECNICO", "-1");
            editor.commit();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Se ha producido un error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
