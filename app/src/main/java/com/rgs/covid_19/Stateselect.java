package com.rgs.covid_19;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Stateselect extends AppCompatActivity {

    ArrayList<String> statelist = new ArrayList<String>();
    ArrayList<String> statedislist = new ArrayList<String>();

    String myResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stateselect);

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

                Stateselect.this.runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
//                        TextView tv = findViewById(R.id.tv);
//                        tv.setTextIsSelectable(true);
//                        tv.setMovementMethod(new ScrollingMovementMethod());
//                        tv.setText(myResponse);
                        Log.d("Suppose", myResponse);
                        try {

                            final JSONObject jsonObject = new JSONObject(myResponse);
//                            Iterator<String> iter = jsonObject.keys();
//                            while(iter.hasNext()) {
//                                String key = iter.next();
//                                Log.d("Suppose" , iter.next());
//                            }
                            JSONArray jsonArray = jsonObject.getJSONArray("key_values");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject details = jsonArray.getJSONObject(i);
                                String confirmeddelta = details.getString("confirmeddelta");
                                String counterforautotimeupdate = details.getString("counterforautotimeupdate");
                                String deceaseddelta = details.getString("deceaseddelta");
                                String lastupdatedtime = details.getString("lastupdatedtime");
                                String recovereddelta = details.getString("recovereddelta");
                                String statesdelta = details.getString("statesdelta");



                                Log.d("Suppose", confirmeddelta);
                                Log.d("Suppose", counterforautotimeupdate);
                                Log.d("Suppose", deceaseddelta);
                                Log.d("Suppose", lastupdatedtime);
                                Log.d("Suppose", recovereddelta);
                                Log.d("Suppose", statesdelta);

                            }


//                            Iterator<String> iter = jsonObject.keys();
//                            while(iter.hasNext()) {
//                                String key = iter.next();
//                                Log.d("Suppose" , iter.next());
//                            }
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


                            Log.d("Suppose123", active);


                            try {
                                JSONObject state_wise = jsonObject.getJSONObject("state_wise");

                                for (String key : iterate(state_wise.keys())) {
                                    statelist.add(key);
                                    Log.d("Suppose", key);
                                }
                                stateselectdialog();
                                Log.d("Supposelen", statelist.size() + "");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            try {

                                JSONObject state_wise = jsonObject.getJSONObject("state_wise");
                                JSONObject state_data = state_wise.getJSONObject("Andhra Pradesh");
                                JSONObject state_data_in = state_data.getJSONObject("district");
                                for (String key : iterate(state_data_in.keys())) {

                                    Log.d("Supposelast", key);
                                }

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
                Statedistlistfun(statelist.get(position));
                Toast.makeText(Stateselect.this, statelist.get(position), Toast.LENGTH_SHORT).show();
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


        try {
            JSONObject jsonObject = new JSONObject(myResponse);
            JSONObject state_wise = jsonObject.getJSONObject("state_wise");
            JSONObject state_data = state_wise.getJSONObject(st);
            JSONObject state_data_in = state_data.getJSONObject("district");

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
                Toast.makeText(Stateselect.this, statedislist.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.show();
        dialog.getWindow().setAttributes(lp);
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

    private <T> Iterable<T> iterate(final Iterator<T> i) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return i;
            }
        };
    }
}
