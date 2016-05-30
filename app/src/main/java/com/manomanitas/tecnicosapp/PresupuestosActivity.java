package com.manomanitas.tecnicosapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.manomanitas.tecnicosapp.PresupuestosPackage.presupuesto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PresupuestosActivity extends AppCompatActivity {


    private PresupuestosTask mAuthTask = null;
    private final String SHARED_PREFS_FILE = "manomanitasConf";

    private SharedPreferences sharedpreferences;
    private HttpURLConnection urlConnection;

    private View mProgressView;
    private View mPresupuestosFormView;


    private List<presupuesto> lista_presupuestos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reciclerview_presupuestos);

        sharedpreferences = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        mPresupuestosFormView = findViewById(R.id.rv);
        mProgressView = findViewById(R.id.presupuestos_progress);

        obtenerPresupuestos();

        RVAdapter adapter = new RVAdapter(lista_presupuestos);
        rv.setAdapter(adapter);

    }

    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PresupuestoViewHolder> {

        public class PresupuestoViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView tw_categoria;
            TextView tw_municipio;
            TextView tw_provincia;
            TextView tw_averia;
            TextView tw_precio;
            TextView tw_kilometros;
            TextView tw_hacedias;
            Button bt_enviarPropuesta;

            PresupuestoViewHolder(View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.cv);
                tw_categoria = (TextView) itemView.findViewById(R.id.idCategoria);
                tw_municipio = (TextView) itemView.findViewById(R.id.idMunicipio);
                tw_provincia = (TextView) itemView.findViewById(R.id.idProvincia);
                tw_averia = (TextView) itemView.findViewById(R.id.idAveria);
                tw_precio = (TextView) itemView.findViewById(R.id.idPrecio);
                tw_kilometros = (TextView) itemView.findViewById(R.id.idKilometros);
                tw_hacedias = (TextView) itemView.findViewById(R.id.idHaceDias);
                bt_enviarPropuesta = (Button) itemView.findViewById(R.id.enviarPropuesta_bt);

            }
        }

        List<presupuesto> presupuestos;

        RVAdapter(List<presupuesto> presupuestos) {
            this.presupuestos = presupuestos;
        }

        @Override
        public PresupuestoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_presupuestos, parent, false);
            PresupuestoViewHolder pvh = new PresupuestoViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(PresupuestoViewHolder holder, int position) {
            holder.tw_categoria.setText(presupuestos.get(position).getCategoria());
            holder.tw_municipio.setText(presupuestos.get(position).getMunicipio());
            holder.tw_provincia.setText(presupuestos.get(position).getProvincia());
            holder.tw_averia.setText(presupuestos.get(position).getAveria());
            holder.tw_kilometros.setText(presupuestos.get(position).getKilometros());
            holder.tw_precio.setText(presupuestos.get(position).getPrecio());
            holder.tw_hacedias.setText(presupuestos.get(position).getHaceDias());

                holder.bt_enviarPropuesta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        enviarP();
                    }
                });

        }

        private void enviarP() {
            Intent intent = new Intent(com.manomanitas.tecnicosapp.PresupuestosActivity.this, DetallePresupuestoActivity.class);
            startActivity(intent);
        }

        @Override
        public int getItemCount() {
            return presupuestos.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

    }

    private void obtenerPresupuestos() {
        lista_presupuestos.add(new presupuesto("0", "Calefaccion", "Valencia", "(Valencia)", "Necesito instalar un Aire acondicionado de 4000 Frigorias, donde la maquina interior de la exterior las separa un tabique, y la maquina exterior ademas va situada en un balcon en un primer piso, en Xirivella", "Ya comprado", "3.2 Km", "hace 3 dias"));
        lista_presupuestos.add(new presupuesto("1", "Fontanero", "Albal", "(Valencia)", "Fuga en un tubo de cobre. La fuga está a la vista, puesto que al hacer un agujero con el taladro, fue taladrado dicho tubo", "¡Gratis!", "10.3 Km", "hace 7 dias"));
        lista_presupuestos.add(new presupuesto("2", "Electricista", "Manises", "(Valencia)", "necesito el boletin electrico e instalacion de luz en una cochera. Llevaria un punto de luz y un enchufe.", "¡Gratis!", "0.8 Km", "hace 5 dias"));

        /*boolean conexion = checkInternet();

        if (conexion) {
            //Cargar información obtenida de shared preferences y cargamos los datos
            String id = sharedpreferences.getString("ID_TECNICO", "-1");
            showProgress(true);
            mAuthTask = new PresupuestosTask(id);
            mAuthTask.execute((Void) null);
        }*/
    }

    private boolean checkInternet(){

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(PresupuestosActivity.this);

            builder.setMessage("Compruebe su conexión a internet")
                    .setTitle("Error de red");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            return false;

        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mPresupuestosFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mPresupuestosFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mPresupuestosFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mPresupuestosFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login task used to authenticate
     * the user.
     */
    public class PresupuestosTask extends AsyncTask<Void, Void, Boolean> {

        private final String idTecnico;

        PresupuestosTask(String id) {
            idTecnico = id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                String url_base = sharedpreferences.getString("URL_BASE", "");

                StringBuilder sb = new StringBuilder();
                sb.append(url_base);
                sb.append("presupuestos.php?");
                sb.append("idTecnico=");
                sb.append(idTecnico);

                String urlLogin = sb.toString();

                URL url = new URL(urlLogin);
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader response = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String presupuesto = response.readLine();

                if(presupuesto.equals("Error")){

                    //Mensaje error

                } else if (presupuesto.equals("No hay presupuestos")){

                    //Mensaje error (falta si devuelve un 0)
                } else{
                    //Añadir el primer presupuesto obtenido y con el bucle el resto

                    while ((presupuesto = response.readLine()) != null) {
                        //lista_presupuestos.add(new presupuesto("0", "Calefaccion", "Valencia", "(Valencia)", "Necesito instalar un Aire acondicionado de 4000 Frigorias, donde la maquina interior de la exterior las separa un tabique, y la maquina exterior ademas va situada en un balcon en un primer piso, en Xirivella", "Ya comprado", "3.2 Km", "hace 3 dias"));

                    }
                }


            } catch (Exception e) {
                return false;

            } finally {
                urlConnection.disconnect();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (success) {


            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(PresupuestosActivity.this);

                builder.setMessage("No se ha podido obtener la informacion")
                        .setTitle("Error");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}

