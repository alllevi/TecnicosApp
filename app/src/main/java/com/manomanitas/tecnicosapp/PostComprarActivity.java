package com.manomanitas.tecnicosapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PostComprarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comprar);
    }

    public void onClick_verComprados(View view) {
        Intent intent = new Intent(this, CompradosActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClick_seguirComprando(View view) {
        Intent intent = new Intent(this, PresupuestosActivity.class);
        startActivity(intent);
        finish();
    }
}
