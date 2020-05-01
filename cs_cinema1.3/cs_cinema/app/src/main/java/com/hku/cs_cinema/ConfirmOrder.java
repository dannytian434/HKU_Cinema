package com.hku.cs_cinema;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class ConfirmOrder extends AppCompatActivity implements View.OnClickListener {
    TextView tv;
    String Email;
    String Subtotal;
    Button btn_confirm;
    RadioButton radioBtn_cd;
    RadioButton radioBtn_pt;
    private static final String TAG = "MakeOrder";
    public static int subtotal = 0;
    private static int TIME_OUT = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        tv = (TextView) findViewById(R.id.summary);
        radioBtn_cd = (RadioButton)findViewById(R.id.credit_card);
        radioBtn_pt = (RadioButton)findViewById(R.id.points);
        btn_confirm = (Button) findViewById(R.id.confirm);
        btn_confirm.setOnClickListener(this);
        Email = Main2Activity.mAccount;
        tv.append("\nAccount name: " + Main2Activity.mAccount);
        tv.append("\nMovie: " + SelectMovie.m_name );
        tv.append("\nDate: " + SelectMovie.m_date + "\nTime: " + SelectMovie.m_time);
        tv.append("\nSeats: ");
        for (int r = 0; r < SelectSeat.row_selected.size(); r++) {
            tv.append("\nRow: " + SelectSeat.row_selected.get(r) +
                    "; Column: " + SelectSeat.column_selected.get(r) + "; Price: HKD" + SelectMovie.m_price +".");
        }
        try {
            subtotal = Integer.parseInt(SelectMovie.m_price) * SelectSeat.row_selected.size();
            Subtotal=Integer.toString(subtotal);
        }catch (NumberFormatException ex){
            Toast.makeText(ConfirmOrder.this, "Sorry! System cannot proceed your order at this moment, try again later!", Toast.LENGTH_SHORT).show();
        }
    }


    public void onClick(View v) {
        //pass data to DB
        Log.i(TAG, "Order!");
        if (radioBtn_cd.isChecked()){
            Intent intent = new Intent(getBaseContext(), Creditcard.class);
            startActivity(intent);
        }else if (radioBtn_pt.isChecked()){
//            new CheckPointsActivity().execute();
            new InsertDataActivity().execute();
            Intent intent = new Intent(getBaseContext(), Thankyou.class);
            startActivity(intent);

        }else {
            Toast.makeText(ConfirmOrder.this, "Please select payment method!", Toast.LENGTH_SHORT).show();
        }
    }
    // TODO: Implement pay by pints logic here.
    //check points
    class CheckPointsActivity extends AsyncTask< Void, Void, Void > {

        ProgressDialog dialog;
        int jIndex;
        int x;
        String result = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ConfirmOrder.this);
            dialog.setTitle("Thanks for your patience...");
            dialog.setMessage("Checking your account..");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void...params) {
            JSONObject jsonObject = Configuration.validateData(Email,Subtotal);
            Log.i(Configuration.TAG, "Json obj ");
            try {
                if (jsonObject != null) {
                    result = jsonObject.getString("result");
                }
            } catch (JSONException je) {
                Log.i(Configuration.TAG, "" + je.getLocalizedMessage());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog != null && ! dialog.isShowing()) {
                dialog.cancel();
            }
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }
    class InsertDataActivity extends AsyncTask< Void, Void, Void > {
        ProgressDialog dialog;
        String result = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
                if (dialog == null) {
                    dialog = new ProgressDialog(ConfirmOrder.this);
                    dialog.setTitle("Thanks for your patience...");
                    dialog.setMessage("Requesting new orders for you...");
                    dialog.setIndeterminate(false);
                    dialog.setCancelable(false);
                }
                dialog.show();
        }

        @Override
        protected Void doInBackground(Void...params) {
            for (int r = 0; r < SelectSeat.row_selected.size(); r++) {
            JSONObject jsonObject = Configuration.orderRequest(SelectMovie.m_name,SelectMovie.m_date
                            +" "+SelectMovie.m_time,SelectSeat.row_selected.get(r),
                    SelectSeat.column_selected.get(r),Main2Activity.mAccount,"Points",
                    SelectMovie.m_price);
                Log.i(Configuration.TAG, "Json obj ");

            try {
                if (jsonObject != null) {
                    result = jsonObject.getString("result");
                }
            } catch (JSONException je) {
                Log.i(Configuration.TAG, "" + je.getLocalizedMessage());
            }

            try {
                JSONObject jsonObject1 = Configuration.reserveSeats(
                        Integer.toString(SelectMovie.m_id), SelectSeat.row_selected.get(r).toString(),
                        Integer.toString(Integer.parseInt(SelectSeat.column_selected.get(r))+1));
            }catch (IOException ex) {
                Toast.makeText(ConfirmOrder.this, "Seats not reserved", Toast.LENGTH_SHORT).show();
            }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(ConfirmOrder.this, result, Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.cancel();
                    dialog.dismiss();
                    finish();
                }
            }, TIME_OUT);

        }
    }}
