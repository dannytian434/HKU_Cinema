package com.hku.cs_cinema;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.hku.cs_cinema.model.Seat;
import com.hku.cs_cinema.model.SeatInfo;
import com.hku.cs_cinema.view.SSThumView;
import com.hku.cs_cinema.view.SSView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SelectSeat extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog mProgress=null;
    private static final int ROW = 35;
    private static final int EACH_ROW_COUNT =19;
    private SSView mSSView;
    private SSThumView mSSThumView;
    private ArrayList<SeatInfo> list_seatInfos = new ArrayList<SeatInfo>();
    private ArrayList<ArrayList<Integer>> list_seat_conditions = new ArrayList<ArrayList<Integer>>();
    public static ArrayList<String> list_seat_string = new ArrayList<String>();//to store the seat cells' character
    public static ArrayList<String> row_selected = new ArrayList<String>();
    public static ArrayList<String> column_selected = new ArrayList<String>();

    Button btn_order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_seat);

        this.mProgress = ProgressDialog.show(this,"Welcome to HKU CS Cinema", "Collecting seat list ...Please wait...",true,false);
        init();
        btn_order=(Button)findViewById(R.id.order);
        btn_order.setOnClickListener(this);
    }
    public void onClick(View v) {
        Intent intent = new Intent(getBaseContext(), ConfirmOrder.class);
        startActivity(intent);
        mProgress.cancel();
        mProgress.dismiss();
        finish();
    }

    private void init(){
        mSSView = (SSView)this.findViewById(R.id.mSSView);
        mSSThumView = (SSThumView)this.findViewById(R.id.ss_ssthumview);
//		mSSView.setXOffset(20);
        setSeatInfo();
        mSSView.init(EACH_ROW_COUNT, ROW, list_seatInfos, list_seat_conditions, mSSThumView, 5);
        mSSView.setOnSeatClickListener(new OnSeatClickListener() {

            @Override
            public boolean b(int column_num, int row_num, boolean paramBoolean) {
                row_selected.add(String.valueOf(row_num+1));
                column_selected.add(String.valueOf(column_num+1));
                if (row_selected != null && !row_selected.isEmpty() && column_selected != null && !column_selected.isEmpty()) {
                    String desc =  "You have selected Row: "+row_selected.get(row_selected.size()-1)+" Column: " + column_selected.get(row_selected.size()-1);
                    Toast.makeText(getBaseContext(),desc.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(),"No rows/columns selected to book", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean a(int column_num, int row_num, boolean paramBoolean) {
                if (row_selected != null && !row_selected.isEmpty() && column_selected != null && !column_selected.isEmpty()){
                    for (int r=0; r<row_selected.size();r++){
                        if (row_selected.get(r)==String.valueOf(row_num+1) && column_selected.get(r)==String.valueOf(column_num+1)){
                            row_selected.remove(r);
                            column_selected.remove(r);
                        }
                    }
                    String desc =  "You have cancelled Row: "+(row_num+1)+" Column: " + (column_num+1);
                    Toast.makeText(getBaseContext(),desc.toString(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getBaseContext(),"No rows/columns selected to cancel", Toast.LENGTH_SHORT).show();
                }
                return false;
            }



            @Override
            public void a() {
                // TODO Auto-generated method stub

            }

        });
    }
    private void setSeatInfo(){
        getResultsFromApi();

    }

    //call getresultapi
    private void getResultsFromApi() {
        //check if network is interruptted before loading data from google
        if (! isDeviceOnline()) {
            Toast.makeText(getBaseContext(),"No network connection available.",
                    Toast.LENGTH_SHORT).show();
        } else {
            new MakeRequestTask(Main2Activity.mCredential).execute();
        }
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
    @AfterPermissionGranted(Main2Activity.REQUEST_PERMISSION_GET_ACCOUNTS)

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case Main2Activity.REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                       Toast.makeText(getBaseContext(),
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app."
                            , Toast.LENGTH_SHORT).show();

                } else {
                    getResultsFromApi();
                }
                break;
            case Main2Activity.REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(this.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(Main2Activity.PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        Main2Activity.mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case Main2Activity.REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
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
                this,
                connectionStatusCode,
                Main2Activity.REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    //AsyncTask to get googlesheet data for seat condition.
    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.sheets.v4.Sheets mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Retrieve Latest Seat List")
                    .build();
        }

        /**
         * Background task to call Google Sheets API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of names and majors of students in a sample spreadsheet:
         * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
         * @return List of names and majors
         * @throws IOException
         */

        private List<String> getDataFromApi() throws IOException {
            String spreadsheetId = "1K6h26_-fGyBhSZbULX_EHbexg2FZWuKl7Th36FekONw";
            String range = "seats"+SelectMovie.m_id+"!B6:T35";
//            ArrayList<String> results = new ArrayList<String>();
            ValueRange response = this.mService.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            List<List<Object>> values = response.getValues();
            if (values != null) {
                for (List row : values) {
                    for (int j = 0; j < EACH_ROW_COUNT; j++) {
                        list_seat_string.add(row.get(j).toString());
                    }
                }
            }
            return list_seat_string;
        }

        @Override
        protected void onPreExecute() {
//            mOutputText.setText("");
            mProgress.setMessage("Loading Seats Lists...");
            mProgress.setIndeterminate(false);
            mProgress.setCancelable(true);
            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            mProgress.hide();
            if (output == null || output.size() == 0) {
                String desc =  "No results returned.";
                Toast.makeText(getBaseContext(),desc.toString(), Toast.LENGTH_SHORT).show();
            } else {
                String desc =  "Seats list updated. Don't book seats for multiple movies," +
                        "please complete one order before next";
                Toast.makeText(getBaseContext(),desc.toString(), Toast.LENGTH_LONG).show();
                if (list_seat_string.isEmpty()) {
                    mProgress.setMessage("Empty list");
                    mProgress.show();
                    return;
                }
                else {
                    int k=0;
                    for (int i = 0; i < ROW; i++) {//
                        SeatInfo mSeatInfo = new SeatInfo();
                        ArrayList<Seat> mSeatList = new ArrayList<Seat>();
                        ArrayList<Integer> mConditionList = new ArrayList<Integer>();
                        Seat mSeat = new Seat();
                        if (i < 5) {
                            mSeat.setN("Z");
                            mConditionList.add(0);
                        } else {
                            for (int j = 0; j < EACH_ROW_COUNT; j++) {
                                mSeat.setN(String.valueOf(j + 1));
                                if ( "-".equals(list_seat_string.get(k))) {
                                    mConditionList.add(1); //available
                                } else {
                                    mConditionList.add(2); //reserved
                                }
                                mSeat.setDamagedFlg("");
                                mSeat.setLoveInd("0");
                                mSeatList.add(mSeat);
                                k++;
                            }
                        }
                        mSeatInfo.setDesc(String.valueOf(i + 1));
                        mSeatInfo.setRow(String.valueOf(i + 1));
                        mSeatInfo.setSeatList(mSeatList);
                        list_seatInfos.add(mSeatInfo);
                        list_seat_conditions.add(mConditionList);
                    }
                }
            }
            list_seat_string.clear();
        }
        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            Main2Activity.REQUEST_AUTHORIZATION);
                } else {
                    String desc =  "The following error occurred:\n"
                            + mLastError.getMessage();
                    Toast.makeText(getBaseContext(),desc.toString(), Toast.LENGTH_SHORT).show();
                }
            } else {
                String desc =  "Request cancelled.";
                Toast.makeText(getBaseContext(),desc.toString(), Toast.LENGTH_SHORT).show();
            }
        }

    }
}
