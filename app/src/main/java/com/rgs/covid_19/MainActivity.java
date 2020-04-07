package com.rgs.covid_19;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
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


import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private final String CHANNEL_ID = "simple notification";

    ArrayList<String> list_state_wise = new ArrayList<String>();
    ArrayList<String> list_dist_wise = new ArrayList<String>();
    private List<MOdel> states;

    String myResponse;

    private TextView india_active_tv;
    private TextView india_deaths_tv;
    private TextView india_recovered_tv;
    private TextView india_confirmed;
    private TextView state_active_tv;
    private TextView state_death_tv;
    private TextView state_recovered_tv;
    private TextView state_confirmed_tv;
    TextView lastupdated_data_tv, selected_state_name, selected_dist, selected_dist_number;
    private MyAdapter adapter;
    private RecyclerView rv;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
    int cases;
    private FirebaseAnalytics mFirebaseAnalytics;
    private TextView delta_india_confirmed;
    private TextView delta_state_confirmed;
    private TextView delta_india_recovered;
    private TextView delta_india_deaths;
    private TextView delta_state_recovered;
    private TextView delta_state_deaths;
    private TextView lastupdated_dist_tv;
    private TextView arrow1;
    private TextView arrow2;
    private TextView arrow3;
    private TextView arrow4;
    private TextView arrow5;
    private TextView arrow6;


    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    CharSequence s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        states = new ArrayList<>();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

    //    startService(new Intent(getBaseContext(), Increasesercive.class));


        //  MobileAds.initialize(this, "ca-app-pub-4057093668636373~8007114541");

        sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);
        editor = sharedPreferences.edit();
        rv = (RecyclerView) findViewById(R.id.recview);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(MainActivity.this);

        india_active_tv = findViewById(R.id.active_total_tv);
        india_deaths_tv = findViewById(R.id.deaths_total_tv);
        india_recovered_tv = findViewById(R.id.recovered_total_tv);
        india_confirmed = findViewById(R.id.total_total_tv);

        state_active_tv = findViewById(R.id.active_state_tv);
        state_death_tv = findViewById(R.id.deaths_state_tv);
        state_recovered_tv = findViewById(R.id.recovered_state_tv);
        state_confirmed_tv = findViewById(R.id.total_state_tv);

        lastupdated_data_tv = findViewById(R.id.lastupdated);
        lastupdated_dist_tv = findViewById(R.id.lastupdated_dist);

        selected_state_name = findViewById(R.id.state_name);
        selected_dist = findViewById(R.id.city_conformed);
        selected_dist_number = findViewById(R.id.city_conformed_num);

        delta_india_confirmed = findViewById(R.id.todaysindia);
        delta_india_recovered = findViewById(R.id.recovered_delataincrease);
        delta_india_deaths = findViewById(R.id.deathgs_delataincrease);
        delta_state_recovered = findViewById(R.id.recovered_delataincrease_dist);
        delta_state_deaths = findViewById(R.id.deathgs_delataincrease_dist);
        delta_state_confirmed = findViewById(R.id.delataincrease_dist);

        arrow1 = findViewById(R.id.arrow1);
        arrow2 = findViewById(R.id.arrow2);
        arrow3 = findViewById(R.id.arrow3);
        arrow4 = findViewById(R.id.arrow4);
        arrow5 = findViewById(R.id.arrow5);
        arrow6 = findViewById(R.id.arrow6);

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


                            editor.putString("conformed_india", confirmed);
                            editor.apply();

                            Date d = new Date();
                            s = DateFormat.format("dd/MM/yyyy", d.getTime());
                            String[] parts = lastupdatedtime.split(" ");

                            if (parts[0].equals(s)){
                                lastupdated_data_tv.setText("Last updated: Today " + parts[1]);
                            } else {
                                lastupdated_data_tv.setText("Last updated: " + lastupdatedtime);
                            }

                            if (deltaconfirmed.equals("0")) {
                                delta_india_confirmed.setVisibility(View.GONE);
                                arrow1.setVisibility(View.GONE);
                            }

                            if (deltadeaths.equals("0")) {
                                delta_india_deaths.setVisibility(View.GONE);
                                arrow3.setVisibility(View.GONE);
                            }

                            if (deltarecovered.equals("0")) {
                                delta_india_recovered.setVisibility(View.GONE);
                                arrow2.setVisibility(View.GONE);
                            }

                            delta_india_recovered.setText(deltarecovered);
                            delta_india_deaths.setText(deltadeaths);
                            delta_india_confirmed.setText(deltaconfirmed);

                            india_active_tv.setText(active);
                            india_deaths_tv.setText(deaths);
                            india_recovered_tv.setText(recovered);
                            india_confirmed.setText(confirmed);


                            list_state_wise.clear();


                            try {
                                JSONObject state_wise = jsonObject.getJSONObject("state_wise");

                                for (String key : iterate(state_wise.keys())) {
                                    list_state_wise.add(key);
                                }

                                if (!sharedPreferences.getBoolean("firstTime", false)) {
                                    stateselectdialog();
                                    editor.putBoolean("firstTime", true);
                                    editor.apply();
                                    rec();
                                } else {
                                    distdataset();
                                    rec();
                                }

                                editor.putString("data", myResponse);
                                editor.apply();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });

        createNotificationChannels();
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d("Firebaseeeeeeeeeeeee", "Refreshed token: " + refreshedToken);

    }


    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence charSequence = "Notification";
            String notidisc = "Noti disc";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, charSequence, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(notidisc);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
            // The id of the group.
            String groupId = "my_group_01";
// The user-visible name of the group.
            CharSequence groupName = "Hello";
            NotificationManager mnotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mnotificationManager.createNotificationChannelGroup(new NotificationChannelGroup(groupId, groupName));
        }


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

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_state_wise);

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editor.putString("state", list_state_wise.get(position));
                editor.apply();
                Statedistlistfun(list_state_wise.get(position));
                Toast.makeText(MainActivity.this, list_state_wise.get(position), Toast.LENGTH_SHORT).show();
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

        list_dist_wise.clear();

        try {
            JSONObject jsonObject = new JSONObject(myResponse);
            JSONObject state_wise = jsonObject.getJSONObject("state_wise");
            JSONObject state_data = state_wise.getJSONObject(st);
            JSONObject state_data_in = state_data.getJSONObject("district");


            for (String key : iterate(state_data_in.keys())) {

                list_dist_wise.add(key);
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

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_dist_wise);

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //setchange(position);
                editor.putString("dist", list_dist_wise.get(position));
                editor.apply();
                distdataset();
                Toast.makeText(MainActivity.this, list_dist_wise.get(position), Toast.LENGTH_SHORT).show();
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

    public void distdataset() {
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

            String delta_confirmed = state_data.getString("deltaconfirmed");
            String delta_deaths = state_data.getString("deltadeaths");
            String delta_recovered = state_data.getString("deltarecovered");
            String lastupdatedtime_dist = state_data.getString("lastupdatedtime");
            String state = state_data.getString("state");


            if (delta_confirmed.equals("0")) {
                delta_state_confirmed.setVisibility(View.GONE);
                arrow4.setVisibility(View.GONE);
            }

            if (delta_recovered.equals("0")) {
                delta_state_recovered.setVisibility(View.GONE);
                arrow5.setVisibility(View.GONE);
            }

            if (delta_deaths.equals("0")) {
                delta_state_deaths.setVisibility(View.GONE);
                arrow6.setVisibility(View.GONE);
            }

            Date d = new Date();
            CharSequence c = DateFormat.format("dd/MM/yyyy", d.getTime());
            String[] parts = lastupdatedtime_dist.split(" ");

            if (parts[0].equals(c)){
                lastupdated_dist_tv.setText("Last updated: Today " + parts[1]);
            } else {
                lastupdated_dist_tv.setText("Last updated: " + lastupdatedtime_dist);
            }

            delta_state_recovered.setText(delta_recovered);
            delta_state_deaths.setText(delta_deaths);
            delta_state_confirmed.setText(delta_confirmed);

            state_death_tv.setText(deaths);
            state_recovered_tv.setText(recovered);
            state_confirmed_tv.setText(confirmed);
            state_active_tv.setText(active);
            selected_state_name.setText(state + " Today");
            selected_dist_number.setText(state_dist_data_in.getString("confirmed"));
            cases = numberFormat.parse(state_dist_data_in.getString("confirmed")).intValue();

            if (!sharedPreferences.getBoolean("firstTime", false)) {
                editor.putBoolean("firstTime", true);
                editor.apply();
            } else {
                if (sharedPreferences.getInt("cases", 0) < cases) {
                    if (sharedPreferences.getString("dist", "Guntur").equals(selected_dist.getText())) {
                        increasedialog();
                    }
                }

            }
            editor.putInt("cases", cases);
            editor.apply();
            selected_dist.setText(sharedPreferences.getString("dist", "Guntur"));

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void rec() {
        try {
            JSONObject jsonObject = new JSONObject(myResponse);
            JSONObject state_wise = jsonObject.getJSONObject("state_wise");

            for (String key : iterate(state_wise.keys())) {

                if (!key.equals("Unknown")) {
                    JSONObject state_data = state_wise.getJSONObject(key);
                    String state = state_data.getString("state");
                    String active = state_data.getString("active");
                    String confirmed = state_data.getString("confirmed");
                    String deaths = state_data.getString("deaths");
                    String recovered = state_data.getString("recovered");
                    String conformedtoday = state_data.getString("deltaconfirmed");
                    String deathstoday = state_data.getString("deltadeaths");
                    String recoveredtoday = state_data.getString("deltarecovered");
                    states.add(new MOdel(active, confirmed, deaths, recovered, state, conformedtoday, deathstoday, recoveredtoday));
                    adapter.setlist(states);
                    rv.setAdapter(adapter);
                    list_dist_wise.add(key);
                }


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

        editor.putString("flag", (String) cas.getText());
        editor.apply();

        cas.setText("There have been " + num + " cases in " + sharedPreferences.getString("dist", "Guntur") + " since the last time you've opened the app");
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
        } else if (item.getItemId() == R.id.feedback) {
            startActivity(new Intent(MainActivity.this, Feedback.class));
        } else if (item.getItemId() == R.id.about) {
            startActivity(new Intent(MainActivity.this, About.class));
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
