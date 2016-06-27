package com.manomanitas.tecnicosapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
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

public class CompradosActivity extends AppCompatActivity {


    private CompradosTask mAuthTask = null;
    private final String SHARED_PREFS_FILE = "manomanitasConf";

    private SharedPreferences sharedpreferences;
    private HttpURLConnection urlConnection;

    private View mProgressView;
    private View mCompradosFormView;

    private List<presupuesto> lista_comprados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reciclerview_presupuestos_comprados);

        //Obtenemos sharedPreferences
        sharedpreferences = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);

        RecyclerView rv = (RecyclerView) findViewById(R.id.rvc);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        mCompradosFormView = findViewById(R.id.rvc);
        mProgressView = findViewById(R.id.comprados_progress);

        //Metodo que realiza la llamada para obtener los presupuestos comprados
        obtenerPresupuestosComprados();

        RVAdapterComprados adapter = new RVAdapterComprados(lista_comprados);
        rv.setAdapter(adapter);
    }

    public class RVAdapterComprados extends RecyclerView.Adapter<RVAdapterComprados.CompradosViewHolder> {

        //Defino variables
        List<presupuesto> comprados;
        RVAdapterComprados(List<presupuesto> list_comprados) {
            this.comprados = list_comprados;
        }


        public class CompradosViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView tw_categoria;
            TextView tw_municipio;
            TextView tw_provincia;
            TextView tw_averia;
            TextView tw_kilometros;
            TextView tw_hacedias;
            TextView tw_nombre;
            TextView tw_telefono;
            TextView tw_email;

            CompradosViewHolder(View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.cvc);
                tw_categoria = (TextView) itemView.findViewById(R.id.idCategoriaComprados);
                tw_municipio = (TextView) itemView.findViewById(R.id.idMunicipioComprados);
                tw_provincia = (TextView) itemView.findViewById(R.id.idProvinciaComprados);
                tw_nombre = (TextView) itemView.findViewById(R.id.idNombreComprados);
                tw_telefono = (TextView) itemView.findViewById(R.id.idTelefonoComprados);
                tw_email = (TextView) itemView.findViewById(R.id.idEmailComprados);
                tw_averia = (TextView) itemView.findViewById(R.id.idAveriaComprados);
                tw_kilometros = (TextView) itemView.findViewById(R.id.idKilometrosComprados);
                tw_hacedias = (TextView) itemView.findViewById(R.id.idHaceDiasComprados);

            }
        }


        @Override
        public CompradosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_presupuestos_comprados, parent, false);
            CompradosViewHolder pvh = new CompradosViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(CompradosViewHolder holder, int position) {
            holder.tw_categoria.setText(comprados.get(position).getCategoria());
            holder.tw_municipio.setText(comprados.get(position).getMunicipio());
            holder.tw_provincia.setText(comprados.get(position).getProvincia());
            holder.tw_averia.setText(comprados.get(position).getAveria());
            holder.tw_nombre.setText(comprados.get(position).getNombre());
            holder.tw_telefono.setText(comprados.get(position).getTelefono());
            holder.tw_email.setText(comprados.get(position).getEmail());
            //holder.tw_kilometros.setText(comprados.get(position).getKilometros());
            holder.tw_hacedias.setText(comprados.get(position).getHaceDias());


        }

        @Override
        public int getItemCount() {
            return comprados.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

    }

    private void obtenerPresupuestosComprados() {

        boolean conexion = checkInternet();

        if (conexion) {
            //Cargar información obtenida de shared preferences y cargamos los datos
            String id = sharedpreferences.getString("ID_TECNICO", "-1");

            showProgress(true);
            mAuthTask = new CompradosTask(id);
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

            mCompradosFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mCompradosFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCompradosFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mCompradosFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous load task presupuestos comprados
     */
    public class CompradosTask extends AsyncTask<Void, Void, Boolean> {

        private final String idTecnico;
        private String datosArray[];
        private String presupuestoArray[];

        CompradosTask(String id) {

            idTecnico = id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                String url_base = sharedpreferences.getString("URL_BASE", "");

                StringBuilder sb = new StringBuilder();
                sb.append(url_base);
                sb.append("comprados.php?");
                sb.append("id=");
                sb.append(idTecnico);

                String urlLogin = sb.toString();

                URL url = new URL(urlLogin);
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                StringBuilder sb_response = new StringBuilder();

                String line = buffer.readLine();

                if(line.equals("0")){

                    //Mensaje error
                    return false;

                } else if (line.equals("No hay presupuestos")){

                    //Mensaje no hay presupuestos
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

                        //Obtenemos los datos de cada presupuesto
                        presupuestoArray = datosArray[i].split("~~");

                        /*Datos devueltos
                            -> categoria, ciudad, provincia, nombre, telefono, email, descripcion, fecha
                         */

                        //Transformamos la fecha
                        String haceDias = formatDate(presupuestoArray[7]);

                        try {
                            lista_comprados.add(new presupuesto(presupuestoArray[0], presupuestoArray[1], presupuestoArray[2], presupuestoArray[6], haceDias, presupuestoArray[3], presupuestoArray[4], presupuestoArray[5]));
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
                Toast.makeText(getApplicationContext(), "No hay presupuestos comprados", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
