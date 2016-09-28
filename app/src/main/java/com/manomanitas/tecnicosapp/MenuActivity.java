package com.manomanitas.tecnicosapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MenuActivity extends AppCompatActivity {

    private final String SHARED_PREFS_FILE = "manomanitasConf";
    private SharedPreferences sharedpreferences;

    private darDeBajaTask mAuthTask = null;
    private HttpURLConnection urlConnection;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "Menu activity";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    String activado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Obtenemos objeto sharedPreferences
        sharedpreferences = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);

        //Si no tenemos registrado un servicio de notificaciones, mandamos la solicitud
        activado = sharedpreferences.getString("GCM", "false");

        if (activado.equals("false")) {

            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    SharedPreferences sharedPreferences =
                            getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
                    boolean sentToken = sharedPreferences
                            .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                    if (sentToken) {
                        Toast.makeText(getApplicationContext(), "Notificaciones activadas", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("GCM", "true");
                        editor.commit();

                    } else {
                        Toast.makeText(getApplicationContext(), "Error al activar notificaciones", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            // Registering BroadcastReceiver
            registerReceiver();

            if (checkPlayServices()) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("¿Cerrar la aplicación?")
                .setMessage("¿Esta seguro que desea cerrar la aplicación?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        MenuActivity.super.onBackPressed();
                    }
                }).create().show();
    }

    @Override
    protected void onPause() {
        if (activado.equals("false")) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
            isReceiverRegistered = false;
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (activado.equals("false")) {
            registerReceiver();
        }
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void onClick_presupuestos(View view) {

        try {
            Intent intent = new Intent(this, PresupuestosActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "No se ha podido acceder a presupuestos", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    public void onClick_comprados(View view) {

        try {
            Intent intent = new Intent(this, CompradosActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "No se ha podido acceder a presupuestos comprados", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void onClick_miPerfil(View view) {

        try {
            Intent intent = new Intent(this, EditarPerfilActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "No se ha podido acceder a editar perfil", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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

    private boolean checkInternet() {

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Compruebe su conexión a internet", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void cerrarSesion() {
        try {

            boolean conexion = checkInternet();

            if (conexion) {
                //Obtenemos id del tecnico de shared preferences y cargamos los datos
                String id = sharedpreferences.getString("ID_TECNICO", "-1");
                mAuthTask = new darDeBajaTask(id);
                mAuthTask.execute((Void) null);
            }

            //Establecemos el id del tecnico a -1
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("ID_TECNICO", "-1");
            editor.putString("GCM", "false");
            editor.commit();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Se ha producido un error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * Represents an asynchronous dar de baja task
     * the user.
     */
    public class darDeBajaTask extends AsyncTask<Void, Void, Boolean> {

        private final String idTecnico;

        darDeBajaTask(String id) {
            idTecnico = id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                String url_base = sharedpreferences.getString("URL_BASE", "");
                StringBuilder sb = new StringBuilder();
                sb.append(url_base);
                sb.append("baja_tecnico.php?");
                sb.append("idTecnico=");
                sb.append(idTecnico);

                String urlBaja = sb.toString();
                URL url = new URL(urlBaja);
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line = buffer.readLine();

                if (line.equals("Se ha borrado")) {
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
            mAuthTask = null;
            if (success) {
                //NADA
            } else {
                Toast.makeText(getApplicationContext(), "Se ha producido un error inesperado", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
