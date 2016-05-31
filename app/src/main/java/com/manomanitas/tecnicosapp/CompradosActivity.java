package com.manomanitas.tecnicosapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

import com.manomanitas.tecnicosapp.PresupuestosPackage.presupuesto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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

        sharedpreferences = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);

        RecyclerView rv = (RecyclerView) findViewById(R.id.rvc);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        mCompradosFormView = findViewById(R.id.rvc);
        mProgressView = findViewById(R.id.comprados_progress);

        obtenerPresupuestosComprados();

        RVAdapterComprados adapter = new RVAdapterComprados(lista_comprados);
        rv.setAdapter(adapter);
    }

    public class RVAdapterComprados extends RecyclerView.Adapter<RVAdapterComprados.CompradosViewHolder> {

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

        List<presupuesto> comprados;

        RVAdapterComprados(List<presupuesto> list_comprados) {
            this.comprados = list_comprados;
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
        //lista_comprados.add(new presupuesto("0", "Electro", "Valencia", "(Valencia)", "asdasdasda", "0.6 Km", "hace 3 dias", "Pepe Perez", "676589898", "peperez@gmail.com"));
        //lista_comprados.add(new presupuesto("1", "Calefaccion", "Valencia", "(Valencia)", "agdfhfgj", "1.2 Km", "hace 7 dias", "Manolo perales", "676512354", "maper@gmail.com"));
        //lista_comprados.add(new presupuesto("2", "Telefonillo", "Valencia", "(Valencia)", "llkljkh", "3.0 Km", "hace 5 dias", "Florentino", "677899898", "floren@gmail.com"));

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

            AlertDialog.Builder builder = new AlertDialog.Builder(CompradosActivity.this);

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
     * Represents an asynchronous login task used to authenticate
     * the user.
     */
    public class CompradosTask extends AsyncTask<Void, Void, Boolean> {

        private final String idTecnico;
        private String datosArray[];

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
                BufferedReader response = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String presupuesto = response.readLine().trim();
                if(presupuesto.equals("0")){

                    //Mensaje error

                } else if (presupuesto.equals("No hay presupuestos")){

                    //Mensaje no hay presupuestos

                } else{
                    //Datos devueltos
                    //Categoria,ciudad,provincia,nombre,telefono,email,descripcion,fecha

                    datosArray = presupuesto.split("~~");
                    lista_comprados.add(new presupuesto(datosArray[0],datosArray[1],datosArray[2],datosArray[6],datosArray[7],datosArray[3],datosArray[4],datosArray[5]));
                    Log.d("excepcion",presupuesto);
                    Log.d("excepcion",datosArray.length+"");
                    Log.d("excepcion",datosArray[0]);
                    Log.d("excepcion",datosArray[1]);
                    Log.d("excepcion",datosArray[2]);
                    Log.d("excepcion",datosArray[3]);
                    Log.d("excepcion",datosArray[4]);
                    Log.d("excepcion",datosArray[5]);
                    Log.d("excepcion",datosArray[6]);
                    Log.d("excepcion",datosArray[7]);
                    Log.d("excepcion","---------");
                    while ((presupuesto = response.readLine().trim()) != null) {

                        datosArray = presupuesto.split("~~");

                        Log.d("excepcion",presupuesto);
                        Log.d("excepcion",datosArray.length+"");
                        Log.d("excepcion",datosArray[0]);
                        Log.d("excepcion",datosArray[1]);
                        Log.d("excepcion",datosArray[2]);
                        Log.d("excepcion",datosArray[3]);
                        Log.d("excepcion",datosArray[4]);
                        Log.d("excepcion",datosArray[5]);
                        Log.d("excepcion",datosArray[6]);
                        Log.d("excepcion",datosArray[7]);
                        Log.d("excepcion","---------");
                        lista_comprados.add(new presupuesto(""+datosArray[0],""+datosArray[1],""+datosArray[2],""+datosArray[6],""+datosArray[7],""+datosArray[3],""+datosArray[4],""+datosArray[5]));

                    }
                }

                return true;

            } catch (Exception e) {
                Log.d("expception",""+e);
                return false;

            } finally {
                urlConnection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (success) {


            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(CompradosActivity.this);

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
