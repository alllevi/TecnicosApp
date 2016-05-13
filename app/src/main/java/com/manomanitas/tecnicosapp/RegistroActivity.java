package com.manomanitas.tecnicosapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class RegistroActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mNombreView;
    private EditText mDniView;
    private EditText mTelefonoView;
    private EditText mCodigoPostalView;
    private EditText mRadioView;

    private Spinner spinnerProvincia;
    private Spinner spinnerMunicipio;

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
    private View mLoginFormView;

    /*TAREAS
    * Datos reales spinners
    * Seleccion de spinners
    * Al menos un checkBox
    * Paso de datos
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mNombreView = (EditText) findViewById(R.id.nombre_registro);
        mDniView = (EditText) findViewById(R.id.dni_registro);
        mTelefonoView = (EditText) findViewById(R.id.telefono_registro);
        mCodigoPostalView = (EditText) findViewById(R.id.codigoPostal_registro);
        mRadioView = (EditText) findViewById(R.id.radio_registro);

        spinnerProvincia = (Spinner) findViewById(R.id.provincia_registro);
        spinnerMunicipio = (Spinner) findViewById(R.id.municipio_registro);

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

        mLoginFormView = findViewById(R.id.registro_form);
        mProgressView = findViewById(R.id.registro_progress);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.registro_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterP = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapterP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerProvincia.setAdapter(adapterP);


// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterM = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapterM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerMunicipio.setAdapter(adapterM);
    }

    /**
     * Attempts to register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mNombreView.setError(null);
        mDniView.setError(null);
        mTelefonoView.setError(null);
        mCodigoPostalView.setError(null);
        mRadioView.setError(null);


        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String nombre = mNombreView.getText().toString();
        String dni = mDniView.getText().toString();
        String telefono = mTelefonoView.getText().toString();
        String codigoPostal = mCodigoPostalView.getText().toString();
        String radio = mRadioView.getText().toString();
        //faltan elementos

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

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password_signIn));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(nombre)) {
            mNombreView.setError(getString(R.string.error_invalid_password_signIn));
            focusView = mNombreView;
            cancel = true;
        }

        if (TextUtils.isEmpty(dni)) {
            mDniView.setError(getString(R.string.error_field_required));
            focusView = mDniView;
            cancel = true;
        } else if (!isDniValid(dni)) {
            mDniView.setError(getString(R.string.error_invalid_email));
            focusView = mDniView;
            cancel = true;
        }

        if (TextUtils.isEmpty(telefono)) {
            mTelefonoView.setError(getString(R.string.error_invalid_password_signIn));
            focusView = mTelefonoView;
            cancel = true;
        } else if (!isTelefonoValid(telefono)) {
            mTelefonoView.setError(getString(R.string.error_invalid_password_signIn));
            focusView = mTelefonoView;
            cancel = true;
        }

        if (TextUtils.isEmpty(codigoPostal)) {
            mCodigoPostalView.setError(getString(R.string.error_invalid_password_signIn));
            focusView = mCodigoPostalView;
            cancel = true;
        } else if (!isCodigoPostalValid(codigoPostal)) {
            mCodigoPostalView.setError(getString(R.string.error_invalid_password_signIn));
            focusView = mCodigoPostalView;
            cancel = true;
        }

        if (TextUtils.isEmpty(radio)) {
            mRadioView.setError(getString(R.string.error_invalid_password_signIn));
            focusView = mRadioView;
            cancel = true;
        } else if (!isRadioValid(radio)) {
            mRadioView.setError(getString(R.string.error_invalid_password_signIn));
            focusView = mRadioView;
            cancel = true;
        }

        //spineers y al menos un check


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password, nombre, dni, telefono, codigoPostal, radio);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return true;
        //return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isDniValid(String dni) {
        return true;
    }

    private boolean isTelefonoValid(String telefono) {
        return true;
    }

    private boolean isCodigoPostalValid(String cp) {
        return true;
    }

    private boolean isRadioValid(String radio) {
        return true;
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

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous registration task
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mNombre;
        private final String mDni;
        private final String mTelefono;
        private final String mCodigoPostal;
        private final String mRadio;


        UserLoginTask(String email, String password, String nombre, String dni, String telefono, String cp, String radio) {
            mEmail = email;
            mPassword = password;
            mNombre = nombre;
            mDni = dni;
            mTelefono = telefono;
            mCodigoPostal = cp;
            mRadio = radio;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

