package com.rgs.covid_19_indian;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> list_state_wise = new ArrayList<>();
    ArrayList<String> list_dist_wise = new ArrayList<>();
    private List<MOdel> states;
    String myResponse, world_myResponse;

    TextView lastupdated_data_tv, selected_state_name, selected_dist, selected_dist_number;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
    private MyAdapter adapter;
    private RecyclerView rv;
    int cases;
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
    private TextView india_active_tv;
    private TextView india_deaths_tv;
    private TextView india_recovered_tv;
    private TextView india_confirmed;
    private TextView state_active_tv;
    private TextView state_death_tv;
    private TextView state_recovered_tv;
    private TextView state_confirmed_tv;
    private TextView worldConfirmed;
    private TextView worldRecovered;
    private TextView worldDeaths;
    private TextView newArrow1;
    private TextView newWorldConfirmed;
    private TextView newArrow3;
    private TextView newWorldDeaths;
    private TextView newArrow9;
    private TextView newDistConfirmed;
    private LinearLayout stateLayout;
    private LinearLayout lytProgress;
    private LinearLayout worldLayout;

    CharSequence s;
    DecimalFormat formatter;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        states = new ArrayList<>();

//        Intent serviceIntent = new Intent(MainActivity.this, MyFirebaseInstanceIDService.class);
//        this.startService(serviceIntent);


        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        formatter = new DecimalFormat("#,###,###");

        lytProgress = findViewById(R.id.lyt_progress);
        sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);
        editor = sharedPreferences.edit();
        rv =  findViewById(R.id.recview);
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

        worldConfirmed = findViewById(R.id.world_confirmed);
        worldRecovered = findViewById(R.id.world_recovered);
        worldDeaths = findViewById(R.id.world_deaths);
        newArrow9 = findViewById(R.id.new_arrow9);
        newDistConfirmed = findViewById(R.id.new_dist_confirmed);


        newArrow1 = findViewById(R.id.new_arrow1);
        newWorldConfirmed = findViewById(R.id.new_world_confirmed);
        newArrow3 = findViewById(R.id.new_arrow3);
        newWorldDeaths = findViewById(R.id.new_world_deaths);
        worldLayout = findViewById(R.id.world_layout);
        stateLayout = findViewById(R.id.state_layout);


        arrow1 = findViewById(R.id.arrow1);
        arrow2 = findViewById(R.id.arrow2);
        arrow3 = findViewById(R.id.arrow3);
        arrow4 = findViewById(R.id.arrow4);
        arrow5 = findViewById(R.id.arrow5);
        arrow6 = findViewById(R.id.arrow6);


        //Checking for Internet
        if (!isNetworkAvailable()) {
            myResponse = sharedPreferences.getString("response", "");
            world_myResponse = sharedPreferences.getString("world_response", "");
            dataset();
            worlddataset();
            Toasty.error(this, "Internet not available, Showing previous data ", Toast.LENGTH_LONG, true).show();
        } else {

            OkHttpClient world_client = new OkHttpClient();

            Request world_request = new Request.Builder()
                    .url("https://corona-virus-world-and-india-data.p.rapidapi.com/api")
                    .get()
                    .addHeader("x-rapidapi-host", "corona-virus-world-and-india-data.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "181e3419demshd0ce128fad4555cp134882jsn58cb1669cb39")
                    .build();

            world_client.newCall(world_request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    world_myResponse = response.body().string();
                    editor.putString("world_response", world_myResponse);
                    editor.apply();
                    Log.d("Dataaaaa" , world_myResponse);
                    worlddataset();

                }
            });



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
                    editor.putString("response", myResponse);
                    editor.apply();
                    dataset();

                }
            });
        }

        if (!sharedPreferences.getBoolean("worldlayout" , true)) {
            worldLayout.setVisibility(View.GONE);
        }

        createNotificationChannels();
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d("Firebaseeeeeeeeeeeee", "Refreshed token: " + refreshedToken);

    }

    public void worlddataset(){

        MainActivity.this.runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {

                try {

                    JSONObject jsonObject = new JSONObject(world_myResponse);
                    JSONObject global = jsonObject.getJSONObject("world_total");
                    String world_deaths = global.getString("total_deaths");
                    String world_recovered = global.getString("total_recovered");
                    String world_confirmed = global.getString("total_cases");
                    String world_new_deaths = global.getString("new_deaths");
                    String world_new_confirmed = global.getString("new_cases");

                    if (world_new_deaths.equals("0")) {
                        newWorldDeaths.setVisibility(View.GONE);
                        newArrow3.setVisibility(View.GONE);
                    }

                    if (world_new_confirmed.equals("0")) {
                        newWorldConfirmed.setVisibility(View.GONE);
                        newArrow1.setVisibility(View.GONE);
                    }

                    try {
                        worldDeaths.setText(formatter.format(numberFormat.parse(world_deaths).intValue()));
                        worldConfirmed.setText(formatter.format(numberFormat.parse(world_confirmed).intValue()));
                        worldRecovered.setText(formatter.format(numberFormat.parse(world_recovered).intValue()));
                        newWorldDeaths.setText(formatter.format(numberFormat.parse(world_new_deaths).intValue()));
                        newWorldConfirmed.setText(formatter.format(numberFormat.parse(world_new_confirmed).intValue()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void disworld(){
        if (worldLayout.getVisibility() == View.VISIBLE){
            worldLayout.setVisibility(View.GONE);
            editor.putBoolean("worldlayout" , false);
            menu.findItem(R.id.disableworld).setTitle("Enable World Stats?");
            Toasty.info(this, "World Data hidden ", Toast.LENGTH_SHORT, true).show();
        } else {
            worldLayout.setVisibility(View.VISIBLE);
            editor.putBoolean("worldlayout" , true);
            menu.findItem(R.id.disableworld).setTitle("Disable World Stats?");
        }
        editor.apply();
    }

    public void dataset(){

        MainActivity.this.runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {

                //Toggle
                if (lytProgress.getVisibility() == View.VISIBLE)
                    lytProgress.setVisibility(View.GONE);
                else
                    lytProgress.setVisibility(View.VISIBLE);

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

                    delta_india_recovered.setText(formatter.format(numberFormat.parse(deltarecovered).intValue()));
                    delta_india_deaths.setText(formatter.format(numberFormat.parse(deltadeaths).intValue()));
                    delta_india_confirmed.setText(formatter.format(numberFormat.parse(deltaconfirmed).intValue()));

                    india_active_tv.setText(formatter.format(numberFormat.parse(active).intValue()));
                    india_deaths_tv.setText(formatter.format(numberFormat.parse(deaths).intValue()));
                    india_recovered_tv.setText(formatter.format(numberFormat.parse(recovered).intValue()));
                    india_confirmed.setText(formatter.format(numberFormat.parse(confirmed).intValue()));

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

                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence charSequence = "Notification";
            String notidisc = "Noti disc";
            String CHANNEL_ID = "simple notification";
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

            if (state_data.has("district")){

                JSONObject state_data_in = state_data.getJSONObject("district");

                for (String key : iterate(state_data_in.keys())) {
                    list_dist_wise.add(key);
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
                        editor.putString("dist", list_dist_wise.get(position));
                        editor.apply();
                        distdataset();
                        Toast.makeText(MainActivity.this, list_dist_wise.get(position), Toast.LENGTH_SHORT).show();
                        Toasty.info(MainActivity.this, "You may choose to enable or disable the world stats from the menu!", Toast.LENGTH_LONG, true).show();

                        dialog.dismiss();
                    }
                });

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.show();
                dialog.getWindow().setAttributes(lp);
            } else {
                editor.putString("dist", "0");
                editor.apply();
                Spanned styledText = Html.fromHtml("No positive cases registered in <b>" + st + "<b>");
                Toasty.info(this, styledText, Toast.LENGTH_LONG, true).show();
                distdataset();
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public void distdataset() {

        try {

            JSONObject jsonObject = new JSONObject(myResponse);
            JSONObject state_wise = jsonObject.getJSONObject("state_wise");
            JSONObject state_data = state_wise.getJSONObject(sharedPreferences.getString("state", "Andhra Pradesh"));


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

            delta_state_recovered.setText(formatter.format(numberFormat.parse(delta_recovered).intValue()));
            delta_state_deaths.setText(formatter.format(numberFormat.parse(delta_deaths).intValue()));
            delta_state_confirmed.setText(formatter.format(numberFormat.parse(delta_confirmed).intValue()));

            state_death_tv.setText(formatter.format(numberFormat.parse(deaths).intValue()));
            state_recovered_tv.setText(formatter.format(numberFormat.parse(recovered).intValue()));
            state_confirmed_tv.setText(formatter.format(numberFormat.parse(confirmed).intValue()));
            state_active_tv.setText(formatter.format(numberFormat.parse(active).intValue()));

            selected_state_name.setText(state + " Today :");

            if (state_data.has("district")){
                if (stateLayout.getVisibility() == View.GONE){
                    stateLayout.setVisibility(View.VISIBLE);
                }
                JSONObject state_data_in = state_data.getJSONObject("district");
                JSONObject state_dist_data_in = state_data_in.getJSONObject(sharedPreferences.getString("dist", "0"));

                if (state_dist_data_in.has("delta")) {
                    JSONObject dist_new = state_dist_data_in.getJSONObject("delta");

                    if (dist_new.getString("confirmed").equals("0")){
                        newArrow9.setVisibility(View.GONE);
                        newDistConfirmed.setVisibility(View.GONE);
                    }

                    newDistConfirmed.setText(dist_new.getString("confirmed"));
                }
                selected_dist_number.setText(state_dist_data_in.getString("confirmed"));
                cases = numberFormat.parse(state_dist_data_in.getString("confirmed")).intValue();

                if (!sharedPreferences.getBoolean("firstTime1", false)) {
                    editor.putBoolean("firstTime1", true);
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
                selected_dist.setText(sharedPreferences.getString("dist", "0")+ " :");
            }  else {
                if (stateLayout.getVisibility() == View.VISIBLE){
                    stateLayout.setVisibility(View.GONE);
                }
            }

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
        this.menu = menu;
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
        } else if (item.getItemId() == R.id.disableworld) {
            disworld();
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
