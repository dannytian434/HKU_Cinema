package com.hku.cs_cinema;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.craftman.cardform.Card;
import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Creditcard extends AppCompatActivity {
    private static int TIME_OUT = 10000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditcard);

        CardForm cardForm = (CardForm)findViewById(R.id.cardform);
        TextView txtDes = (TextView)findViewById(R.id.payment_amount);
        Button btnPay = (Button)findViewById(R.id.btn_pay);
        txtDes.setText("HKD"+ConfirmOrder.subtotal);
        btnPay.setText(String.format("Payer %s",txtDes.getText()));
        cardForm.setPayBtnClickListner(new OnPayBtnClickListner() {
            @Override
            public void onClick(Card card) {
                Toast.makeText(Creditcard.this,"Name :"+card.getName()+" | Last 4 digits : "+
                        card.getLast4(),Toast.LENGTH_SHORT).show();
                new InsertDataActivity().execute();
                Intent intent = new Intent(getBaseContext(), Thankyou.class);
                startActivity(intent);
            }
        });

    }
    // TODO: Implement pay by creditcard logic here.
    class InsertDataActivity extends AsyncTask< Void, Void, Void > {
        ProgressDialog dialog;
        String result = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (dialog == null) {
                dialog = new ProgressDialog(Creditcard.this);
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
                JSONObject jsonObject = Configuration.orderRequest(SelectMovie.m_name,SelectMovie.m_date+" "+SelectMovie.m_time,
                        SelectSeat.row_selected.get(r),SelectSeat.column_selected.get(r),Main2Activity.mAccount,"CreditCard",SelectMovie.m_price);
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
                    Toast.makeText(Creditcard.this, "Seats not reserved", Toast.LENGTH_LONG).show();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(Creditcard.this, result, Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.cancel();
                    dialog.dismiss();
                    finish();
                }
            }, TIME_OUT);
        }
    }
}
