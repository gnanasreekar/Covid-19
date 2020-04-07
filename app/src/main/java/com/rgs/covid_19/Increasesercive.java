package com.rgs.covid_19;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Increasesercive extends Service {

    private final String CHANNEL_ID = "simple notification";
    private final int NOTIFICATION_ID = 1000;
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
    SharedPreferences sharedPreferences;


    @Override
    public void onStart(Intent intent, int startId) {
        sharedPreferences = getSharedPreferences("sp", MODE_PRIVATE);
    }


    public Increasesercive() {

//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url("https://corona-virus-world-and-india-data.p.rapidapi.com/api_india")
//                .get()
//                .addHeader("x-rapidapi-host", "corona-virus-world-and-india-data.p.rapidapi.com")
//                .addHeader("x-rapidapi-key", "181e3419demshd0ce128fad4555cp134882jsn58cb1669cb39")
//                .build();
////                Response response = client.newCall(request).execute();
////        Log.e(TAG, response.body().string());
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                String mMessage = e.getMessage().toString();
//                Log.w("Heeloooooo failure", mMessage);
//                //call.cancel();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                String mMessage = response.body().string();
//                Log.e("Heeloooooo", mMessage);
//
//                if (!sharedPreferences.getString("dist", "n").equals("n")) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(mMessage);
//                        JSONObject state_wise = jsonObject.getJSONObject("state_wise");
//                        JSONObject state_data = state_wise.getJSONObject(sharedPreferences.getString("state", "Andhra Pradesh"));
//                        JSONObject state_data_in = state_data.getJSONObject("district");
//                        JSONObject state_dist_data_in = state_data_in.getJSONObject(sharedPreferences.getString("dist", "Guntur"));
//                        int cases = numberFormat.parse(state_dist_data_in.getString("confirmed")).intValue();
//                        int num = cases - sharedPreferences.getInt("cases", 0);
//                        if (sharedPreferences.getInt("cases", 0) < cases) {
//                            if (num > 0) {
//                                NotificationCompat.Builder builder = new NotificationCompat.Builder(Increasesercive.this, CHANNEL_ID);
//                                builder.setSmallIcon(R.drawable.ic_virus);
//                                builder.setContentTitle("Updates");
//                                builder.setContentText("There have been " + num + " conformed cases in " + sharedPreferences.getString("dist", "Your City"));
//                                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
//                                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(Increasesercive.this);
//                                notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
//                            }
//                        }
//
//                        Log.d("Heeloooooo1", String.valueOf(cases));
//                        Log.d("Heeloooooo2", "" + sharedPreferences.getInt("cases", 0));
//
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putInt("cases", cases);
//                        editor.apply();
//
//                    } catch (JSONException | ParseException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
