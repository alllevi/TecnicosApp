package com.manomanitas.tecnicosapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick_entrar(View view) {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

}
