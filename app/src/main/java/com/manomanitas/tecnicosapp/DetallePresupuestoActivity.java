package com.manomanitas.tecnicosapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetallePresupuestoActivity extends AppCompatActivity {

    private TextView mCategoria;
    private TextView mMunicipio;
    private TextView mProvincia;
    private TextView mNombre;
    private TextView mTelefono;
    private TextView mEmail;
    private TextView mAveria;

    private String categoria;
    private String municipio;
    private String provincia;
    private String nombre;
    private String telefono;
    private String email;
    private String averia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_presupuesto);

        //Recogemos la informacion
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            categoria = bundle.getString("categoria");
            municipio = bundle.getString("municipio");
            provincia = bundle.getString("provincia");
            nombre = bundle.getString("nombre");
            telefono = bundle.getString("telefono");
            email = bundle.getString("email");
            averia = bundle.getString("descripcion");
        }

        //Cargamos los elementos de la interfaz
        mCategoria = (TextView) findViewById(R.id.idCategoriaDetalles);
        mMunicipio = (TextView) findViewById(R.id.idMunicipioDetalles);
        mProvincia = (TextView) findViewById(R.id.idProvinciaDetalles);
        mNombre = (TextView) findViewById(R.id.idNombreDetalles);
        mTelefono = (TextView) findViewById(R.id.idTelefonoDetalles);
        mEmail = (TextView) findViewById(R.id.idEmailDetalles);
        mAveria = (TextView) findViewById(R.id.idAveriaDetalles);

        //Establecemos los valores
        mCategoria.setText(categoria);
        mMunicipio.setText(municipio);
        mProvincia.setText("(" + provincia + ")");
        mNombre.setText(nombre);
        mTelefono.setText(telefono);
        mEmail.setText(email);
        mAveria.setText(averia);
    }
}
