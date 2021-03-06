/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.manomanitas.tecnicosapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    private final String SHARED_PREFS_FILE = "manomanitasConf";
    private SharedPreferences sharedPreferences;

    private darDeAltaTask mAuthTask = null;
    private HttpURLConnection urlConnection;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sharedPreferences = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);

        String id = sharedPreferences.getString("ID_TECNICO", "-1");

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            //String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
            //        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            String token = instanceID.getToken(getString(R.string.project_number),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + token);

            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(token,id);

            // Subscribe to topic channels
            //subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token , String id) {
        // Add custom implementation, as needed.

        mAuthTask = new darDeAltaTask(id,token);
        mAuthTask.execute((Void) null);
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    /*private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }*/
    // [END subscribe_topics]

    /**
     * Represents an asynchronous dar de alta task
     * the user.
     */
    public class darDeAltaTask extends AsyncTask<Void, Void, Boolean> {

        private final String idTecnico;
        private final String token;

        darDeAltaTask(String id, String tk) {

            idTecnico = id;
            token = tk;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                //Obtenemos objeto sharedPreferences
                sharedPreferences = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
                String url_base = sharedPreferences.getString("URL_BASE", "");


                StringBuilder sb = new StringBuilder();
                sb.append(url_base);
                sb.append("insertar_token.php?");
                sb.append("idTecnico=");
                sb.append(idTecnico);
                sb.append("&token=");
                sb.append(token);

                String urlAlta = sb.toString();
                Log.d("tagRegister",urlAlta);
                URL url = new URL(urlAlta);
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));


                String line = buffer.readLine();

                if (line.equals("No hay token")) {

                    return false;

                } else if (line.equals("No se pudo modificar tu registro")){

                    return false;

                } else {
                    return true;
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


            } else {
                Toast.makeText(getApplicationContext(), "No se ha podido actualizar el registro de notificaciones", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

}
