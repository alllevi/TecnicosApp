package com.manomanitas.tecnicosapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EditarPerfilActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private GuardarPerfilTask mAuthTask = null;
    private ObtenerPerfilTask mAuthTaskObtener = null;
    private final String SHARED_PREFS_FILE = "manomanitasConf";

    private SharedPreferences sharedpreferences;
    private HttpURLConnection urlConnection;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mNombreView;
    private EditText mDniView;
    private EditText mTelefonoView;
    private EditText mCodigoPostalView;
    private EditText mRadioView;

    private Spinner spinnerProvincia;
    private Spinner spinnerMunicipio;

    private TextView tv_servicios;
    private CheckBox cElectricidad;
    private CheckBox cFontaneria;
    private CheckBox cCerrajero;
    private CheckBox cVideo;
    private CheckBox cAntenista;
    private CheckBox cTelefonillo;
    private CheckBox cClima;
    private CheckBox cCalentador;
    private CheckBox cCalefaccion;
    private CheckBox cAlarma;
    private CheckBox cElectro;

    private CheckBox cAvisos;

    private View mProgressView;
    private View mPerfilFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Obtenemos sharedPreferences
        sharedpreferences = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mNombreView = (EditText) findViewById(R.id.nombre_registro);
        mDniView = (EditText) findViewById(R.id.dni_registro);
        mTelefonoView = (EditText) findViewById(R.id.telefono_registro);
        mCodigoPostalView = (EditText) findViewById(R.id.codigoPostal_registro);
        mRadioView = (EditText) findViewById(R.id.radio_registro);

        spinnerProvincia = (Spinner) findViewById(R.id.provincia_registro);
        spinnerMunicipio = (Spinner) findViewById(R.id.municipio_registro);

        tv_servicios = (TextView) findViewById(R.id.serviciosReg_textView);
        cElectricidad = (CheckBox) findViewById(R.id.electricidad_checkBox);
        cFontaneria = (CheckBox) findViewById(R.id.fontaneria_checkBox);
        cCerrajero = (CheckBox) findViewById(R.id.cerrajeria_checkBox);
        cVideo = (CheckBox) findViewById(R.id.video_checkBox);
        cAntenista = (CheckBox) findViewById(R.id.antenista_checkBox);
        cTelefonillo = (CheckBox) findViewById(R.id.telefono_checkBox);
        cClima = (CheckBox) findViewById(R.id.clima_checkBox);
        cCalentador = (CheckBox) findViewById(R.id.calentador_checkBox);
        cCalefaccion = (CheckBox) findViewById(R.id.calefaccion_checkBox);
        cAlarma = (CheckBox) findViewById(R.id.alarma_checkBox);
        cElectro = (CheckBox) findViewById(R.id.electro_checkBox);

        cAvisos = (CheckBox) findViewById(R.id.avisos_checkBox);

        mPerfilFormView = findViewById(R.id.registro_form);
        mProgressView = findViewById(R.id.registro_progress);

        //Rellenamos los spinners de las provincias
        loadSpinnerProvincias();


        Button mEmailSignInButton = (Button) findViewById(R.id.registro_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptEditarPerfil();
            }
        });

        boolean conexion = checkInternet();

        if (conexion) {
            //Recuperar id del tecnico de sharedpreferences y cargamos los datos del perfil
            String id = sharedpreferences.getString("ID_TECNICO", "-1");
            showProgress(true);
            mAuthTaskObtener = new ObtenerPerfilTask(id);
            mAuthTaskObtener.execute((Void) null);
        }
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

    /**
     * Populate the Spinner.
     */
    private void loadSpinnerProvincias() {

        // Create an ArrayAdapter using the string array and a default spinner
        // layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.provincias, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.spinnerProvincia.setAdapter(adapter);

        // This activity implements the AdapterView.OnItemSelectedListener
        this.spinnerProvincia.setOnItemSelectedListener(this);
        this.spinnerMunicipio.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos,
                               long id) {

        switch (parent.getId()) {
            case R.id.provincia_registro:

                // Retrieves an array
                TypedArray arrayLocalidades = getResources().obtainTypedArray(
                        R.array.array_provincia_a_localidades);
                CharSequence[] localidades = arrayLocalidades.getTextArray(pos);
                arrayLocalidades.recycle();

                // Create an ArrayAdapter using the string array and a default
                // spinner layout
                ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                        this, android.R.layout.simple_spinner_item,
                        android.R.id.text1, localidades);

                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                this.spinnerMunicipio.setAdapter(adapter);

                break;

            case R.id.municipio_registro:

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Callback method to be invoked when the selection disappears from this
        // view. The selection can disappear for instance when touch is
        // activated or when the adapter becomes empty.
    }

    /**
     * Attempts to edit the account specified.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    private void attemptEditarPerfil() {

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mNombreView.setError(null);
        mDniView.setError(null);
        mTelefonoView.setError(null);
        mCodigoPostalView.setError(null);
        mRadioView.setError(null);
        tv_servicios.setError(null);


        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String nombre = mNombreView.getText().toString();
        String dni = mDniView.getText().toString();
        String telefono = mTelefonoView.getText().toString();
        String codigoPostal = mCodigoPostalView.getText().toString();
        String radio = mRadioView.getText().toString();

        String provincia = spinnerProvincia.getSelectedItem().toString();
        String municipio = spinnerMunicipio.getSelectedItem().toString();

        boolean electricidad = cElectricidad.isChecked();
        boolean fontaneria = cFontaneria.isChecked();
        boolean cerrajero = cCerrajero.isChecked();
        boolean video = cVideo.isChecked();
        boolean antenista = cAntenista.isChecked();
        boolean telefonillo = cTelefonillo.isChecked();
        boolean clima = cClima.isChecked();
        boolean calentador = cCalentador.isChecked();
        boolean calefaccion = cCalefaccion.isChecked();
        boolean alarma = cAlarma.isChecked();
        boolean electro = cElectro.isChecked();
        boolean aviso = cAvisos.isChecked();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (TextUtils.isEmpty(nombre)) {
            mNombreView.setError(getString(R.string.error_field_required));
            focusView = mNombreView;
            cancel = true;
        }

        if (TextUtils.isEmpty(dni)) {
            mDniView.setError(getString(R.string.error_field_required));
            focusView = mDniView;
            cancel = true;
        } else if (!isDniValid(dni)) {
            mDniView.setError(getString(R.string.error_incorrect_dni));
            focusView = mDniView;
            cancel = true;
        }

        if (TextUtils.isEmpty(telefono)) {
            mTelefonoView.setError(getString(R.string.error_field_required));
            focusView = mTelefonoView;
            cancel = true;
        } else if (!isTelefonoValid(telefono)) {
            mTelefonoView.setError(getString(R.string.error_incorrect_phone));
            focusView = mTelefonoView;
            cancel = true;
        }

        if (TextUtils.isEmpty(codigoPostal)) {
            mCodigoPostalView.setError(getString(R.string.error_field_required));
            focusView = mCodigoPostalView;
            cancel = true;
        } else if (!isCodigoPostalValid(codigoPostal)) {
            mCodigoPostalView.setError(getString(R.string.error_incorrect_postalCode));
            focusView = mCodigoPostalView;
            cancel = true;
        }

        if (TextUtils.isEmpty(radio)) {
            mRadioView.setError(getString(R.string.error_field_required));
            focusView = mRadioView;
            cancel = true;
        } else if (!isRadioValid(radio)) {
            mRadioView.setError(getString(R.string.error_max_radio));
            focusView = mRadioView;
            cancel = true;
        }

        if (!isProvinciaValid(provincia)) {
            TextView errorText = (TextView) spinnerProvincia.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText(R.string.provincia_prompt);//changes the selected item text to this

            focusView = spinnerProvincia;
            cancel = true;
        }

        if (!isMunicipioValid(municipio)) {

            TextView errorText = (TextView) spinnerMunicipio.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText(R.string.localidad_prompt);//changes the selected item text to this

            focusView = spinnerMunicipio;
            cancel = true;
        }

        boolean checked = !electricidad && !fontaneria && !cerrajero && !video && !antenista && !telefonillo && !clima && !calentador && !calefaccion && !alarma && !electro;

        if (checked) {
            tv_servicios.setError("");
            focusView = tv_servicios;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user registration attempt.
            boolean conexionGuardar = checkInternet();

            if (conexionGuardar) {
                showProgress(true);
                mAuthTask = new GuardarPerfilTask(email, nombre, dni, telefono, codigoPostal, radio, provincia, municipio, String.valueOf(electricidad), String.valueOf(fontaneria), String.valueOf(cerrajero), String.valueOf(video), String.valueOf(antenista), String.valueOf(telefonillo), String.valueOf(clima), String.valueOf(calentador), String.valueOf(calefaccion), String.valueOf(alarma), String.valueOf(electro), String.valueOf(aviso));
                mAuthTask.execute((Void) null);
            }
        }
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isDniValid(String dni) {
        return dni.matches("(([X-Z]{1})([-]?)(\\d{7})([-]?)([A-Z]{1}))|((\\d{8})([-]?)([A-Z]{1}))");
    }

    private boolean isTelefonoValid(String telefono) {
        return Patterns.PHONE.matcher(telefono).matches();
    }

    private boolean isCodigoPostalValid(String cp) {
        return cp.matches("^([1-9]{2}|[0-9][1-9]|[1-9][0-9])[0-9]{3}$");
    }

    private boolean isRadioValid(String radio) {
        return Integer.valueOf(radio) <= 50;

    }

    private boolean isProvinciaValid(String provincia) {
        boolean b = provincia.equals("Seleccione una provincia");
        return !b;
    }

    private boolean isMunicipioValid(String municipio) {
        boolean b = municipio.equals("Seleccione un municipio");
        return !b;

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

            mPerfilFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mPerfilFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mPerfilFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            //mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
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
            mPerfilFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous edit task
     */
    public class GuardarPerfilTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mNombre;
        private final String mDni;
        private final String mTelefono;
        private final String mCodigoPostal;
        private final String mRadio;

        private final String mProvincia;
        private final String mMunicipio;

        // true en el caso de que sí que ofrezca el servicio, false si no lo ofrece
        private final String mElectricidad;
        private final String mFontaneria;
        private final String mCerrajero;
        private final String mVideo;
        private final String mAntenista;
        private final String mTelefonillo;
        private final String mClima;
        private final String mCalentador;
        private final String mCalefaccion;
        private final String mAlarma;
        private final String melectro;

        private final String mAvisos;


        GuardarPerfilTask(String email, String nombre, String dni, String telefono, String cp, String radio, String provincia, String municipio, String electricidad, String fontaneria, String cerrajero, String video, String antentista, String telefonillo, String clima, String calentador, String calefaccion, String alarma, String electro, String avisos) {
            mEmail = email;
            mNombre = nombre;
            mDni = dni;
            mTelefono = telefono;
            mCodigoPostal = cp;
            mRadio = radio;
            mProvincia = provincia;
            mMunicipio = municipio;
            mElectricidad = electricidad;
            mFontaneria = fontaneria;
            mCerrajero = cerrajero;
            mVideo = video;
            mAntenista = antentista;
            mTelefonillo = telefonillo;
            mClima = clima;
            mCalentador = calentador;
            mCalefaccion = calefaccion;
            mAlarma = alarma;
            melectro = electro;
            mAvisos = avisos;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                String url_base = sharedpreferences.getString("URL_BASE", "");
                String id = sharedpreferences.getString("ID_TECNICO", "-1");

                /*Tenemos que convertir los datos para realizar el paso de datos al servidor
                  true -> 1
                  false -> 0
                 */

                String notificaciones = "0";

                if (mAvisos.equals("true")) {
                    notificaciones = "1";
                }

                StringBuilder c = new StringBuilder();

                if (mElectricidad.equals("true")) {
                    c.append("Electricista");
                    c.append(",");
                }
                if (mFontaneria.equals("true")) {
                    c.append("Fontanero");
                    c.append(",");
                }
                if (mCerrajero.equals("true")) {
                    c.append("Cerrajero");
                    c.append(",");
                }
                if (mVideo.equals("true")) {
                    c.append("Videovigilancia");
                    c.append(",");
                }
                if (mAntenista.equals("true")) {
                    c.append("Antenista");
                    c.append(",");
                }
                if (mTelefonillo.equals("true")) {
                    c.append("Telefonillo");
                    c.append(",");
                }
                if (mClima.equals("true")) {
                    c.append("Clima");
                    c.append(",");
                }
                if (mCalentador.equals("true")) {
                    c.append("Calentador");
                    c.append(",");
                }
                if (mCalefaccion.equals("true")) {
                    c.append("Calefaccion");
                    c.append(",");
                }
                if (mAlarma.equals("true")) {
                    c.append("Alarma");
                    c.append(",");
                }
                if (melectro.equals("true")) {
                    c.append("Electro");
                }

                String categorias = c.toString();

                //Eliminamos si se ha quedado una "," al final de la cadena
                String s;
                s = categorias.substring(categorias.length() - 1);

                if (s.equals(",")) {
                    categorias = categorias.substring(0, categorias.length() - 1);
                }

                //Construimos la URL
                StringBuilder sb = new StringBuilder();
                sb.append(url_base);
                sb.append("editar-perfil.php?");
                sb.append("idTecnico=");
                sb.append(id);
                sb.append("&nombre=");
                sb.append(mNombre);
                sb.append("&dni=");
                sb.append(mDni);
                sb.append("&email=");
                sb.append(mEmail);
                sb.append("&telefono=");
                sb.append(mTelefono);
                sb.append("&cpostal=");
                sb.append(mCodigoPostal);
                sb.append("&radiocp=");
                sb.append(mRadio);
                sb.append("&provincia=");
                sb.append(mProvincia);
                sb.append("&direccion=");
                sb.append(mMunicipio);
                sb.append("&notificaciones=");
                sb.append(notificaciones);
                sb.append("&categorias=");
                sb.append(categorias);

                String urlEditar = sb.toString();

                URL url = new URL(urlEditar);
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader response = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String presupuesto = response.readLine();

                if (presupuesto.equals("Se actualizaron tus datos")) {
                    return true;
                } else {
                    return false;
                }

            } catch (Exception e) {
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
                Toast.makeText(getApplicationContext(), "Datos actualizados", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(), "No han habido cambios en su perfil", Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * Represents an asynchronous load task
     */
    public class ObtenerPerfilTask extends AsyncTask<Void, Void, Boolean> {

        private final String idTecnico;
        private String datosArray[];

        ObtenerPerfilTask(String id) {
            idTecnico = id;

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                //Construimos la URL
                String url_base = sharedpreferences.getString("URL_BASE", "");
                StringBuilder sb = new StringBuilder();
                sb.append(url_base);
                sb.append("perfil.php?");
                sb.append("idTecnico=");
                sb.append(idTecnico);
                String urlLogin = sb.toString();

                URL url = new URL(urlLogin);
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader response = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String datosPerfil = response.readLine();

                //Parseamos los datos que viene separados por comas
                if (!datosPerfil.equals("Error")) {
                    datosArray = datosPerfil.split(",");
                    return true;
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
            mAuthTaskObtener = null;
            showProgress(false);

            if (success) {

                //Rellenamos el formulario

                int longitud = datosArray.length;

                mEmailView.setText(datosArray[2]);
                mNombreView.setText(datosArray[0]);
                mDniView.setText(datosArray[1]);
                mTelefonoView.setText(datosArray[3]);
                mCodigoPostalView.setText(datosArray[4]);
                mRadioView.setText(datosArray[5]);

                if (datosArray[8].equals("1")) {
                    cAvisos.setChecked(true);
                }

                int contador = 9; //los servicios empiezan en la posicion 9 del array
                while (contador < longitud) {

                    if (datosArray[contador].equals("Electricista")) {
                        cElectricidad.setChecked(true);
                    } else if (datosArray[contador].equals("Fontanero")) {
                        cFontaneria.setChecked(true);
                    } else if (datosArray[contador].equals("Cerrajero")) {
                        cCerrajero.setChecked(true);
                    } else if (datosArray[contador].equals("Videovigilancia")) {
                        cVideo.setChecked(true);
                    } else if (datosArray[contador].equals("Antenista")) {
                        cAntenista.setChecked(true);
                    } else if (datosArray[contador].equals("Telefonillo")) {
                        cTelefonillo.setChecked(true);
                    } else if (datosArray[contador].equals("Clima")) {
                        cClima.setChecked(true);
                    } else if (datosArray[contador].equals("Calentador")) {
                        cCalentador.setChecked(true);
                    } else if (datosArray[contador].equals("Calefaccion")) {
                        cCalefaccion.setChecked(true);
                    } else if (datosArray[contador].equals("Alarma")) {
                        cAlarma.setChecked(true);
                    } else if (datosArray[contador].equals("Electro")) {
                        cElectro.setChecked(true);
                    }
                    contador++;
                }
            } else {

                Toast.makeText(getApplicationContext(), "No se ha podido obtener la información", Toast.LENGTH_SHORT).show();
                mEmailView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTaskObtener = null;
            showProgress(false);
        }
    }

}
