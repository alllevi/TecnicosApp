package com.manomanitas.tecnicosapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private final String SHARED_PREFS_FILE = "manomanitasConf";
    private final String URL_BASE = "http://www.manomanitas.es/solicitar-presupuesto/appmovil/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedpreferences = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("URL_BASE", URL_BASE);
        editor.commit();

        String id = sharedpreferences.getString("ID_TECNICO", "-1");
        if(!id.equals("-1")){
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        }
    }

    public void onClick_entrar(View view) {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

}
