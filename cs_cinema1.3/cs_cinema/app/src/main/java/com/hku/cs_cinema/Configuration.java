package com.hku.cs_cinema;

/**
 * Created by ADJ on 8/9/2017.
 */

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Configuration {

    public static final String APP_SCRIPT_WEB_APP_URL = "https://script.google.com/macros/s/AKfycbyGOJnkBDdbl9h8TPYOxwBxoFuUOXSg3gGCpRK1DanjzzJ9MyY/exec";
    public static final String APP_SCRIPT_RESERVE_SEATS_URL = "https://script.google.com/macros/s/AKfycbxp3l8DIcKvfyr1tBb4JHSBGc55fikeTEkq_bH-992AOo7OL1s/exec";
    public static final String ADD_USER_URL = APP_SCRIPT_WEB_APP_URL;
    public static final String VALIDATE_USER_URL = APP_SCRIPT_WEB_APP_URL;
    public static final String MAKE_ORDER_URL = APP_SCRIPT_WEB_APP_URL;
//    public static final String LIST_USER_URL = APP_SCRIPT_WEB_APP_URL+"?action=readAll";

//    public static final String KEY_ID = "uId";
    public static final String KEY_NAME = "Name";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_PWD = "Password";
    public static final String KEY_POINTS = "Points";
//    public static final String KEY_USERS = "records";
    public static final String KEY_MNAME = "Movie_Name";
    public static final String KEY_MDate = "Show_Date";
    public static final String KEY_ROW = "Seat_Row";
    public static final String KEY_COLUMN = "Seat_Column";
    public static final String KEY_PRICE = "Price";
    public static final String KEY_SUBTOTAL = "Subtotal";

    public static final String KEY_PAYMENT = "Payment";

//reserve seets
    public static final String KEY_MID = "Movie_ID";


    public  static final String KEY_ACTION = "action";
    private static Response response;
    private static boolean success;
    public static final String TAG = "TAG";

    public static JSONObject insertData(String name, String email, String pwd,String points) {

        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add(KEY_ACTION,"insert")
                    .add(KEY_NAME, name)
                    .add(KEY_EMAIL,email)
                    .add(KEY_PWD,pwd)
                    .add(KEY_POINTS, points)
                    .build();
            Request request = new Request.Builder()
                    .url(ADD_USER_URL)
                    .post(formBody)
                    .build();
            response = client.newCall(request).execute();
            //    Log.e(TAG,"response from gs"+response.body().string());
            String jsonData =response.body().string();
            return new JSONObject(jsonData);
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "recieving null " + e.getLocalizedMessage());
        }
        return null;
    }
    public static JSONObject validateData(String email,String subtotal)  {

        try {

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add(KEY_ACTION,"validate")
                    .add(KEY_EMAIL,email)
                    .add(KEY_SUBTOTAL,subtotal)
                    .build();
            Request request = new Request.Builder()
                    .url(VALIDATE_USER_URL)
                    .post(formBody)
                    .build();
            response = client.newCall(request).execute();
            //    Log.e(TAG,"response from gs"+response.body().string());
            String jsonData =response.body().string();
            return new JSONObject(jsonData);
        }
        catch (Exception e) {
            Log.e(TAG, "recieving null " + e.getLocalizedMessage());
        }
        return null;
    }
    public static JSONObject orderRequest(String mname, String mdate, String mrow, String mcolumn,String email,String payment,String price) {

        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add(KEY_ACTION,"order")
                    .add(KEY_MNAME,mname)
                    .add(KEY_MDate, mdate)
                    .add(KEY_ROW,mrow)
                    .add(KEY_COLUMN,mcolumn)
                    .add(KEY_EMAIL,email)
                    .add(KEY_PAYMENT, payment)
                    .add(KEY_PRICE,price)
                    .build();
            Request request = new Request.Builder()
                    .url(MAKE_ORDER_URL)
                    .post(formBody)
                    .build();
            response = client.newCall(request).execute();
            //    Log.e(TAG,"response from gs"+response.body().string());
            String jsonData =response.body().string();
            return new JSONObject(jsonData);

        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "recieving null " + e.getLocalizedMessage());
        }
        response.close();
        return null;
    }
    public static JSONObject reserveSeats(String mid,String mrow,String mcolumn) throws IOException {

        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add(KEY_ACTION,"update")
                    .add(KEY_MID,mid)
                    .add(KEY_ROW,mrow)
                    .add(KEY_COLUMN,mcolumn)
                    .build();
            Request request = new Request.Builder()
                    .url(APP_SCRIPT_RESERVE_SEATS_URL)
                    .post(formBody)
                    .build();
            response = client.newCall(request).execute();
            //    Log.e(TAG,"response from gs"+response.body().string());
            String jsonData =response.body().string();
            return new JSONObject(jsonData);

        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "recieving null " + e.getLocalizedMessage());
        }
        response.close();
        return null;
    }

}
