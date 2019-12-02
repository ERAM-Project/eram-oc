package org.eram.oc.logger.sheets;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;

import org.eram.oc.logger.db.LogLine;

import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ERAMSheets implements EasyPermissions.PermissionCallbacks {

    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    static final int REQUEST_AUTHORIZATION = 1001;
    private static final String PREF_ACCOUNT_NAME = "accountName";

    private GoogleAccountCredential googleAccountCredential;

    private Activity activity;
    private static final String[] SCOPES = { SheetsScopes.SPREADSHEETS};
    private static final String accountName ="houssemeddine.mazouzi@gmail.com";

    public ERAMSheets(Activity activity)
    {
        this.activity = activity;

        // Initialize credentials and service object.
        googleAccountCredential = GoogleAccountCredential.usingOAuth2(
                activity.getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        googleAccountCredential.setSelectedAccountName(accountName);
        SharedPreferences settings = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_ACCOUNT_NAME, accountName);
        editor.apply();
        editor.commit();


    }

    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    public void getResultsFromApi(LogLine log) {
        //new GoogleAPIRequest(activity, googleAccountCredential).execute(log);
        AccountManager mgr = AccountManager.get(this.activity);

        for (Account acc: mgr.getAccounts()){//mgr.getAccountsByType("com.google")) {
            Log.e("ACCOUNT", acc.toString());
            Log.e("AACCC", acc.name);
        }

        this.googleAccountCredential.setSelectedAccount(mgr.getAccounts()[0]);
        new GoogleAPIRequest(activity, googleAccountCredential).execute(log);

    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(activity);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(activity);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                activity,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    //@AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {

        SharedPreferences settings =
                activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_ACCOUNT_NAME, accountName);
        editor.apply();
        googleAccountCredential.setSelectedAccountName(accountName);

        /*
        if (EasyPermissions.hasPermissions(
                activity, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = activity.getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {

                //getResultsFromApi();
                Log.e("KOLOPO", "12");

                SharedPreferences settings =
                        activity.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(PREF_ACCOUNT_NAME, accountName);
                editor.apply();
                googleAccountCredential.setSelectedAccountName(accountName);

            } else {
                // Start a dialog from which the user can choose an account
                activity.startActivityForResult(
                        googleAccountCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
                Log.e("KOLOPA1","12");

                SharedPreferences settings =
                        activity.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(PREF_ACCOUNT_NAME, accountName);
                editor.apply();
                googleAccountCredential.setSelectedAccountName(accountName);

            }
        } else {

            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    activity,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);

            Log.e("KOLOPA2", "12");

            SharedPreferences settings =
                    activity.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(PREF_ACCOUNT_NAME, accountName);
            editor.apply();
            googleAccountCredential.setSelectedAccountName(accountName);
        }
        */
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        this.activity.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);

    }

}
