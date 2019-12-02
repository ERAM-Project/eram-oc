package org.eram.oc.logger.sheets;

import android.accounts.Account;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import org.eram.oc.logger.ConfigurationPreferences;
import org.eram.oc.logger.db.LogLine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.login.LoginException;

/**
 * An asynchronous task that handles the Google Sheets API call.
 * Placing the API calls in their own task ensures the UI stays responsive.
 */
public class GoogleAPIRequest extends AsyncTask<LogLine, Void, Void> {

    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;

    private Sheets sheetsServer = null;
    private Exception lastError = null;
    private Activity activity;
    private LogLine log;



   public  GoogleAPIRequest(Activity activity, GoogleAccountCredential credential) {

       this.activity = activity;

       HttpTransport transport = AndroidHttp.newCompatibleTransport();
       JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
       sheetsServer = new com.google.api.services.sheets.v4.Sheets.Builder(
               transport, jsonFactory, credential)
               .setApplicationName("ERAM Google Sheets")
               .build();

    }

    /**
     * Background task to call Google Sheets API.
     * @param params no parameters needed for this task.
     */
    @Override
    protected Void doInBackground(LogLine... params) {
        try {
            this.log = params[0];
             addDataInSheets();
        } catch (Exception e) {
            Log.e("POLOPA",e.toString()+"+"+this.log);
            cancel(true);
        }

        return null;
    }

    /**
     * Fetch a list of names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     * @return List of names and majors
     * @throws IOException
     */
    private void addDataInSheets()
    {
        String spreadsheetId = "1eN6d891kL1E6CCfvhCoboUJNA_bM9SfBb4cfygpVDKo";//ConfigurationPreferences.getSpreadID(activity.getPreferences(Context.MODE_PRIVATE));
        String range = "eram";//ConfigurationPreferences.getRange(activity.getPreferences(Context.MODE_PRIVATE));


        List<List<Object>> values = this.log.asList();

        Log.e(getClass().getName(), log.toString());

        ValueRange body = new ValueRange()
                .setValues(values);

        try {
            AppendValuesResponse result =
                    this.sheetsServer.spreadsheets().values().append(spreadsheetId, range, body)
                            .setValueInputOption("RAW")
                            .execute();
        }catch (IOException e)
        {
            Log.e(this.getClass().getName(), e.toString());
        }
    }


    @Override
    protected void onCancelled() {

        if (lastError != null) {

            if (lastError instanceof GooglePlayServicesAvailabilityIOException) {
                showGooglePlayServicesAvailabilityErrorDialog(
                        ((GooglePlayServicesAvailabilityIOException) lastError)
                                .getConnectionStatusCode());
            } else if (lastError instanceof UserRecoverableAuthIOException) {
                activity.startActivityForResult(
                        ((UserRecoverableAuthIOException) lastError).getIntent(),
                        ERAMSheets.REQUEST_AUTHORIZATION);
            } else {

            }
        } else {

        }
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    private void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                activity,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }
}