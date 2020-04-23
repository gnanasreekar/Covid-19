package com.rgs.covid_19_indian.dist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;

import com.rgs.covid_19_indian.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class Dist extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dist);
        List<Model_dist> dist = new ArrayList<>();
        RecyclerView rv = findViewById(R.id.dist_rec);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter_dist adapter = new MyAdapter_dist(Dist.this);
        sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);
        try {
            JSONObject jsonObject = new JSONObject(sharedPreferences.getString("data", "Guntur"));
            JSONObject state_wise = jsonObject.getJSONObject("state_wise");
            JSONObject state_data = state_wise.getJSONObject(getIntent().getStringExtra("state"));

            if (state_data.has("district")){
                JSONObject state_data_in = state_data.getJSONObject("district");

                for (String key : iterate(state_data_in.keys())) {

                    JSONObject state_dist_data = state_data_in.getJSONObject(key);
                    if (state_dist_data.has("confirmed")){
                        String confirmed = state_dist_data.getString("confirmed");
                        dist.add(new Model_dist(confirmed, key));
                        adapter.setlist(dist);
                        rv.setAdapter(adapter);
                    } else {
                        Toasty.info(this, "Proper Data not available ", Toast.LENGTH_LONG, true).show();
                    }
                }
            } else {

                Spanned styledText = Html.fromHtml("No positive cases registered in <b>" + getIntent().getStringExtra("state") + "<b>");
                Toasty.info(this, styledText, Toast.LENGTH_LONG, true).show();
                finish();
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
