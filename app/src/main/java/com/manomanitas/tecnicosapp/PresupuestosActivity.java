package com.manomanitas.tecnicosapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.manomanitas.tecnicosapp.PresupuestosPackage.presupuesto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PresupuestosActivity extends AppCompatActivity {


    private PresupuestosTask mAuthTask = null;
    private comprarTask mAuthTask2 = null;
    private final String SHARED_PREFS_FILE = "manomanitasConf";

    private SharedPreferences sharedpreferences;
    private HttpURLConnection urlConnection;

    private View mProgressView;
    private View mPresupuestosFormView;

    private Intent intentPostCompra;


    private List<presupuesto> lista_presupuestos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reciclerview_presupuestos);

        //Obtenemos sharedPreferences
        sharedpreferences = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        mPresupuestosFormView = findViewById(R.id.rv);
        mProgressView = findViewById(R.id.presupuestos_progress);

        //Metodo que realiza la llamada para obtener los presupuestos
        obtenerPresupuestos();

        RVAdapter adapter = new RVAdapter(lista_presupuestos);
        rv.setAdapter(adapter);

    }

    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PresupuestoViewHolder> {

        //Definimos variables
        List<presupuesto> presupuestos;
        RVAdapter(List<presupuesto> presupuestos) {
            this.presupuestos = presupuestos;
        }

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

        @Override
        public PresupuestoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_presupuestos, parent, false);
            PresupuestoViewHolder pvh = new PresupuestoViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(PresupuestoViewHolder holder, final int position) {
            holder.tw_categoria.setText(presupuestos.get(position).getCategoria());
            holder.tw_municipio.setText(presupuestos.get(position).getMunicipio());
            holder.tw_provincia.setText("("+presupuestos.get(position).getProvincia()+")");
            holder.tw_averia.setText(presupuestos.get(position).getAveria());
            //holder.tw_kilometros.setText(presupuestos.get(position).getKilometros());
            holder.tw_precio.setText(presupuestos.get(position).getPrecio());
            holder.tw_hacedias.setText(presupuestos.get(position).getHaceDias());

            //Asignamos un listener al boton enviar propuesta
                holder.bt_enviarPropuesta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        enviarP(presupuestos.get(position));
                    }
                });

        }

        /**
         * Metodo que permite pasar a la interfaz detalles prespuesto
         * @param p , presupuesto que contiene la cardview
         * Le pasa en los extras los datos necesarios a la interfaz de detalles
         */
        private void enviarP(presupuesto p) {

            boolean conexion = checkInternet();


            if (conexion) {

                // --- ABRIR PARA PAGAR ---

                ///----------------

                intentPostCompra = new Intent(com.manomanitas.tecnicosapp.PresupuestosActivity.this, PostComprarActivity.class);
                startActivity(intentPostCompra);
                finish();


                /*try {
                    intentComprado = new Intent(com.manomanitas.tecnicosapp.PresupuestosActivity.this, DetallePresupuestoActivity.class);
                    intentComprado.putExtra("categoria", p.getCategoria());
                    intentComprado.putExtra("municipio", p.getMunicipio());
                    intentComprado.putExtra("provincia", p.getProvincia());
                    intentComprado.putExtra("nombre", p.getNombre());
                    intentComprado.putExtra("telefono", p.getTelefono());
                    intentComprado.putExtra("email", p.getEmail());
                    intentComprado.putExtra("descripcion", p.getAveria());

                    //Comprar presupuesto

                    String idTecnico = sharedpreferences.getString("ID_TECNICO", "-1");
                    mAuthTask2 = new comprarTask(idTecnico, p.getId());
                    mAuthTask2.execute((Void) null);


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "No se ha podido acceder a detalles", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }*/

            }
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

        boolean conexion = checkInternet();

        if (conexion) {
            //Obtenemos id del tecnico de shared preferences y cargamos los datos
            String id = sharedpreferences.getString("ID_TECNICO", "-1");
            showProgress(true);
            mAuthTask = new PresupuestosTask(id);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean checkInternet(){

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Compruebe su conexión a internet", Toast.LENGTH_SHORT).show();
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
     * Represents an asynchronous load presupuestos task
     * the user.
     */
    public class PresupuestosTask extends AsyncTask<Void, Void, Boolean> {

        private final String idTecnico;
        private String datosArray[];
        private String presupuestoArray[];

        PresupuestosTask(String id) {

            idTecnico = id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                String url_base = sharedpreferences.getString("URL_BASE", "");

                StringBuilder sb = new StringBuilder();
                sb.append(url_base);
                sb.append("presupuestos_new.php?");
                sb.append("idTecnico=");
                sb.append(idTecnico);

                String urlLogin = sb.toString();
                URL url = new URL(urlLogin);
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                StringBuilder sb_response = new StringBuilder();

                String line = buffer.readLine();

                if(line.equals("Error")){

                    //Mensaje error
                    return false;

                } else if (line.equals("No hay presupuestos")){

                    //Mensaje error (falta si devuelve un 0)
                    return false;

                } else{

                    //recogemos toda la resupuesta en una cadena
                    sb_response.append(line);

                    while ((line = buffer.readLine()) != null) {
                        sb_response.append(line);
                    }

                    String response = sb_response.toString();

                    //Separamos los diferentes presupuestos
                    datosArray = response.split(";_;");

                    for(int i=0;i<datosArray.length;i++){

                        //Obtenemos la informacion por separado de cada presupuesto
                        presupuestoArray = datosArray[i].split("~~");

                        /*Datos devueltos
                            respuesta php ->Categoria, ciudad, provincia, descripcion, nombre, telefono, email, fecha
                         */

                        //Transformamos la fecha
                        String haceDias = formatDate(presupuestoArray[5]);

                        try {
                            lista_presupuestos.add(new presupuesto(presupuestoArray[0], presupuestoArray[1], presupuestoArray[2], presupuestoArray[3], presupuestoArray[4],presupuestoArray[6], haceDias));

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }

                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;

            } finally {
                urlConnection.disconnect();
            }

        }

        private String formatDate(String fecha){

            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                Date datePresupuesto = formatDate.parse(fecha);
                Date now = new Date();
                String d = formatDate.format(now);
                Date dateActual = formatDate.parse(d);

                long diff = dateActual.getTime() - datePresupuesto.getTime();

                long diffDays = diff / (24 * 60 * 60 * 1000);

                String haceDias = "Hace "+diffDays+" días";

                if(haceDias.equals("Hace 0 días")){
                    return "Hoy";
                } else{
                    return haceDias;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (success) {


            } else {
                Toast.makeText(getApplicationContext(), "No hay presupuestos para mostrar", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * Represents an asynchronous comprar task
     * the user.
     */
    public class comprarTask extends AsyncTask<Void, Void, Boolean> {

        private final String idTecnico;
        private final String idPresupuesto;


        comprarTask(String id, String presupuesto) {

            idTecnico = id;
            idPresupuesto = presupuesto;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                String url_base = sharedpreferences.getString("URL_BASE", "");

                StringBuilder sb = new StringBuilder();
                sb.append(url_base);
                sb.append("comprar.php?");
                sb.append("id_tecnico=");
                sb.append(idTecnico);
                sb.append("&id_presupuesto=");
                sb.append(idPresupuesto);

                String urlComprar = sb.toString();
                Log.d("LOG_COMPRADO",urlComprar);

                URL url = new URL(urlComprar);
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                //StringBuilder sb_response = new StringBuilder();
                //Los datos ya los tengo de otra consulta --> mejora en un futuro

                String line = buffer.readLine();

                //¿Que pasa si devuelve un 0?

                if(line.equals("No hay presupuestos comprados")){

                    //Mensaje error
                    return false;

                } else if (line.equals("0")){

                    return false;
                } else if (line.contains("~~")){

                    return true;
                } else {

                    return false;
                }


            } catch (Exception e) {
                e.printStackTrace();
                return false;

            } finally {
                urlConnection.disconnect();
            }

        }

       @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask2 = null;
            showProgress(false);
            if (success) {
                Toast.makeText(getApplicationContext(), "Presupuesto comprado", Toast.LENGTH_SHORT).show();
                //startActivity(intentComprado);

            } else {
                Toast.makeText(getApplicationContext(), "No se ha podido comprar", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask2 = null;
            showProgress(false);
        }
    }

}

