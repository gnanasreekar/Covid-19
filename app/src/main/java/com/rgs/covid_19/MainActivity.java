package com.rgs.covid_19;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private NotificationManagerCompat notificationManager;

    private final String CHANNEL_ID = "simple notification";
    private final int NOTIFICATION_ID = 1000;

    ArrayList<String> statelist = new ArrayList<String>();
    ArrayList<String> statedislist = new ArrayList<String>();
    private List<MOdel> states;

    String myResponse;
    private AdView mAdView;


    private TextView activeTotalTv;
    private TextView deathsTotalTv;
    private TextView recoveredTotalTv;
    private TextView totalTotalTv;
    private TextView activeStateTv;
    private TextView deathsStateTv;
    private TextView recoveredStateTv;
    private TextView totalStateTv;
    TextView lastupdated,statename, citycon, cityconnum;
    private MyAdapter adapter;
    private RecyclerView rv;
    SharedPreferences sharedPreferences;
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
    int cases;
    private FirebaseAnalytics mFirebaseAnalytics;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        states = new ArrayList<>();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //  MobileAds.initialize(this, "ca-app-pub-4057093668636373~8007114541");
        //  startActivity(new Intent(MainActivity.this , Stateselect.class));

        sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);
        rv = (RecyclerView) findViewById(R.id.recview);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(MainActivity.this);
        activeTotalTv = findViewById(R.id.active_total_tv);
        deathsTotalTv = findViewById(R.id.deaths_total_tv);
        recoveredTotalTv = findViewById(R.id.recovered_total_tv);
        totalTotalTv = findViewById(R.id.total_total_tv);
        activeStateTv = findViewById(R.id.active_state_tv);
        deathsStateTv = findViewById(R.id.deaths_state_tv);
        recoveredStateTv = findViewById(R.id.recovered_state_tv);
        totalStateTv = findViewById(R.id.total_state_tv);
        lastupdated = findViewById(R.id.lastupdated);
        statename = findViewById(R.id.state_name);
        citycon = findViewById(R.id.city_conformed);
        cityconnum = findViewById(R.id.city_conformed_num);


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://corona-virus-world-and-india-data.p.rapidapi.com/api_india")
                .get()
                .addHeader("x-rapidapi-host", "corona-virus-world-and-india-data.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "181e3419demshd0ce128fad4555cp134882jsn58cb1669cb39")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                myResponse = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {

                        Log.d("Suppose", myResponse);
                        try {

                            final JSONObject jsonObject = new JSONObject(myResponse);


                            JSONObject valuesarray = jsonObject.getJSONObject("total_values");
                            String active = valuesarray.getString("active");
                            String confirmed = valuesarray.getString("confirmed");
                            String deaths = valuesarray.getString("deaths");
                            String deltaconfirmed = valuesarray.getString("deltaconfirmed");
                            String deltadeaths = valuesarray.getString("deltadeaths");
                            String deltarecovered = valuesarray.getString("deltarecovered");
                            String lastupdatedtime = valuesarray.getString("lastupdatedtime");
                            String recovered = valuesarray.getString("recovered");
                            String state = valuesarray.getString("state");
                            String statecode = valuesarray.getString("statecode");

                            activeTotalTv.setText(active);
                            deathsTotalTv.setText(deaths);
                            recoveredTotalTv.setText(recovered);
                            totalTotalTv.setText(confirmed);
                            lastupdated.setText("Last updated: "+lastupdatedtime);

                            statelist.clear();


                            try {
                                JSONObject state_wise = jsonObject.getJSONObject("state_wise");

                                for (String key : iterate(state_wise.keys())) {
                                    statelist.add(key);
                                    Log.d("Suppose", key);
                                }

                                if (!sharedPreferences.getBoolean("firstTime", false)) {
                                    stateselectdialog();
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("firstTime", true);
                                    editor.apply();
                                    rec();
                                } else {distdataset();rec();}

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("data", myResponse);
                                editor.apply();

                                Log.d("Supposelen", statelist.size() + "");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Log.d("Rapidapi", myResponse);
                    }
                });

            }
        });

        createNotificationChannels();

    }


    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           CharSequence charSequence = "Notification";
           String notidisc = "Noti disc";
           NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,charSequence, NotificationManager.IMPORTANCE_DEFAULT);
           notificationChannel.setDescription(notidisc);

           NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
           notificationManager.createNotificationChannel(notificationChannel);
        }

        createNotificationChannels();

    }


    public void stateselectdialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.select_state);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Statelistfun();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void Statelistfun() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.userslist);
        dialog.setCancelable(true);

        ListView lv;
        lv = (ListView) dialog.findViewById(R.id.listviewaa);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, statelist);

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("state", statelist.get(position));
                editor.apply();
                Statedistlistfun(statelist.get(position));
                Toast.makeText(MainActivity.this, statelist.get(position), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void Statedistlistfun(String st) {

        statedislist.clear();

        try {
            JSONObject jsonObject = new JSONObject(myResponse);
            JSONObject state_wise = jsonObject.getJSONObject("state_wise");
            JSONObject state_data = state_wise.getJSONObject(st);
            JSONObject state_data_in = state_data.getJSONObject("district");

            String active = state_data.getString("active");
            String confirmed = state_data.getString("confirmed");
            String deaths = state_data.getString("deaths");
            String recovered = state_data.getString("recovered");
            String state = state_data.getString("state");

            Log.d("Dataaaa" , active);
            for (String key : iterate(state_data_in.keys())) {

                statedislist.add(key);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.userslist);
        dialog.setCancelable(true);

        ListView lv;
        lv = (ListView) dialog.findViewById(R.id.listviewaa);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, statedislist);

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //setchange(position);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("dist", statedislist.get(position));
                editor.apply();
                distdataset();
                Toast.makeText(MainActivity.this, statedislist.get(position), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void  distdataset(){
        try {
            JSONObject jsonObject = new JSONObject(myResponse);
            JSONObject state_wise = jsonObject.getJSONObject("state_wise");
            JSONObject state_data = state_wise.getJSONObject(sharedPreferences.getString("state", "Andhra Pradesh"));
            JSONObject state_data_in = state_data.getJSONObject("district");
            JSONObject state_dist_data_in = state_data_in.getJSONObject(sharedPreferences.getString("dist", "Guntur"));

            String active = state_data.getString("active");
            String confirmed = state_data.getString("confirmed");
            String deaths = state_data.getString("deaths");
            String recovered = state_data.getString("recovered");
            String state = state_data.getString("state");


            deathsStateTv.setText(deaths);
            recoveredStateTv.setText(recovered);
            totalStateTv.setText(confirmed);
            activeStateTv.setText(active);
            statename.setText(state);
            cityconnum.setText(state_dist_data_in.getString("confirmed"));
            cases = numberFormat.parse(state_dist_data_in.getString("confirmed")).intValue();

            if (!sharedPreferences.getBoolean("firstTime", false)) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("firstTime", true);
                editor.apply();
            } else {
                if(sharedPreferences.getInt("cases", 0) < cases) {
                    if (sharedPreferences.getString("dist", "Guntur").equals(citycon.getText())) {
                        increasedialog();
                    }
                }

            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("cases", cases);
            editor.apply();
            citycon.setText(sharedPreferences.getString("dist", "Guntur"));

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void rec(){
        try {
            JSONObject jsonObject = new JSONObject(myResponse);
            JSONObject state_wise = jsonObject.getJSONObject("state_wise");

            for (String key : iterate(state_wise.keys())) {
                JSONObject state_data = state_wise.getJSONObject(key);
                String active = state_data.getString("active");
                String confirmed = state_data.getString("confirmed");
                String deaths = state_data.getString("deaths");
                String recovered = state_data.getString("recovered");
                String state = state_data.getString("state");
                states.add(new MOdel(active , confirmed , deaths, recovered, state));
                adapter.setlist(states);
                rv.setAdapter(adapter);
                statedislist.add(key);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void increasedialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.increasedialo);
        dialog.setCancelable(true);
        int num = cases - sharedPreferences.getInt("cases", 0);
        TextView cas = dialog.findViewById(R.id.casesadded);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("flag", (String) cas.getText());
        editor.apply();

        cas.setText("There have been " + num +" cases in "+ sharedPreferences.getString("dist", "Guntur") + " since the last time you've opened the app");
        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.changestate) {
            stateselectdialog();
        } else if (item.getItemId() == R.id.feedback){
            startActivity(new Intent(MainActivity.this, Feedback.class));
        } else if (item.getItemId() == R.id.about){
            startActivity(new Intent(MainActivity.this, About.class));
        } else if (item.getItemId() == R.id.noti){
            noti();
        }
        return super.onOptionsItemSelected(item);
    }

    private <T> Iterable<T> iterate(final Iterator<T> i) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return i;
            }
        };
    }
}
