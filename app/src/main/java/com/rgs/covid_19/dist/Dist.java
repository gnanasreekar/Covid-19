package com.rgs.covid_19.dist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.rgs.covid_19.MOdel;
import com.rgs.covid_19.MainActivity;
import com.rgs.covid_19.MyAdapter;
import com.rgs.covid_19.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Dist extends AppCompatActivity {

    private MyAdapter_dist adapter;
    private RecyclerView rv;
    private List<Model_dist> dist;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dist);
        dist = new ArrayList<>();
        rv = (RecyclerView) findViewById(R.id.dist_rec);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter_dist(Dist.this);
        sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);
        try {
            JSONObject jsonObject = new JSONObject(sharedPreferences.getString("data", "Guntur"));
            JSONObject state_wise = jsonObject.getJSONObject("state_wise");
            JSONObject state_data = state_wise.getJSONObject(getIntent().getStringExtra("state"));
            JSONObject state_data_in = state_data.getJSONObject("district");

            for (String key : iterate(state_data_in.keys())) {
                Log.d("ASDF" , key);
                JSONObject state_dist_data = state_data_in.getJSONObject(key);
//                JSONObject detla = state_dist_data.getJSONObject("delta");
//                String detla_confor = detla.getString("confirmed");
//                Log.d("Helloooo" , detla_confor);
                String confirmed = state_dist_data.getString("confirmed");
                dist.add(new Model_dist(confirmed, key));
                adapter.setlist(dist);
                rv.setAdapter(adapter);
                Log.d("ASDF" , confirmed);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
