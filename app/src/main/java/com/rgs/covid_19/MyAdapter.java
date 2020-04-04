package com.rgs.covid_19;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rgs.covid_19.dist.Dist;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<MOdel> listData;
    Context context;

    public void setlist(List<MOdel> listData) {
        this.listData = listData;
    }

    public MyAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lidt_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MOdel ld = listData.get(position);
        final String active = ld.getActive();
        final String recovered = ld.getRecovered();
        final String deaths = ld.getDeaths();
        final String total = ld.getConfirmed();
        final String state = ld.getState();

        holder.recViewActive.setText("Active: "+active);
        holder.recViewDeath.setText("Deaths: "+deaths);
        holder.recViewRecovered.setText("Recovered: "+recovered);
        holder.statenameRec.setText(state);
        holder.recViewTotal.setText("Total: "+total);


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext() , Dist.class);
                intent.putExtra("state" , state);
                v.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        LinearLayout linearLayout;
        private TextView statenameRec;
        private RelativeLayout recViewLayoutTotal;
        private TextView recViewTotal;
        private RelativeLayout recViewLayoutRecovered;
        private TextView recViewRecovered;
        private RelativeLayout recViewLayoutActive;
        private TextView recViewActive;
        private RelativeLayout recViewLayoutDeath;
        private TextView recViewDeath;


        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearlayout_events);
            statenameRec = itemView.findViewById(R.id.statename_rec);
            recViewLayoutTotal = itemView.findViewById(R.id.rec_view_layout_total);
            recViewTotal = itemView.findViewById(R.id.rec_view_total);
            recViewLayoutRecovered = itemView.findViewById(R.id.rec_view_layout_recovered);
            recViewRecovered = itemView.findViewById(R.id.rec_view_recovered);
            recViewLayoutActive = itemView.findViewById(R.id.rec_view_layout_active);
            recViewActive = itemView.findViewById(R.id.rec_view_active);
            recViewLayoutDeath = itemView.findViewById(R.id.rec_view_layout_death);
            recViewDeath = itemView.findViewById(R.id.rec_view_death);
            cardView = itemView.findViewById(R.id.card_view);


        }
    }

}