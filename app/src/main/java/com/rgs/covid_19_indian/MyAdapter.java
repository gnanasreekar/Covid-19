package com.rgs.covid_19_indian;

import android.annotation.SuppressLint;
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

import com.rgs.covid_19_indian.dist.Dist;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<MOdel> listData;
    private Context context;

    void setlist(List<MOdel> listData) {
        this.listData = listData;
    }

    MyAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lidt_data, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat formatter = new DecimalFormat("#,###,###");

        final MOdel ld = listData.get(position);

        try {

            final String recovered = formatter.format(numberFormat.parse(ld.getRecovered()).intValue());
            final String deaths = formatter.format(numberFormat.parse(ld.getDeaths()).intValue());
            final String total = formatter.format(numberFormat.parse(ld.getConfirmed()).intValue());
            final String active = formatter.format(numberFormat.parse(ld.getActive()).intValue());
            holder.recViewActive.setText("Active: "+active);
            holder.recViewDeath.setText("Casualties: "+deaths);
            holder.recViewRecovered.setText("Recovered: "+recovered);
            holder.recViewTotal.setText("Total: "+total);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        final String state = ld.getState();
        final String deltaconformed = ld.getDeltaconformed();
        final String deltadeaths = ld.getDeltadeath();
        final String deltarecovered = ld.getDeltarecovered();

        if (deltaconformed.equals("0")){
            holder.rec_delta_conformed.setVisibility(View.GONE);
            holder.recArrow1.setVisibility(View.GONE);
        }

        if (deltarecovered.equals("0")){
            holder.rec_delta_recovered.setVisibility(View.GONE);
            holder.recArrow2.setVisibility(View.GONE);
        }

        if (deltadeaths.equals("0")){
            holder.rec_delta_deaths.setVisibility(View.GONE);
            holder.recArrow3.setVisibility(View.GONE);
        }

        holder.rec_delta_conformed.setText(deltaconformed);
        holder.rec_delta_recovered.setText(deltarecovered);
        holder.rec_delta_deaths.setText(deltadeaths);

        holder.statenameRec.setText(state);
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
        private TextView rec_delta_conformed;
        private TextView rec_delta_recovered;
        private TextView rec_delta_deaths;
        private TextView recArrow1;
        private TextView recArrow2;
        private TextView recArrow3;





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
            rec_delta_conformed = itemView.findViewById(R.id.rec_delataincrease_dist);
            rec_delta_recovered = itemView.findViewById(R.id.rec_recovered_delataincrease_dist);
            rec_delta_deaths = itemView.findViewById(R.id.rec_deathgs_delataincrease_dist);
            recArrow1 = itemView.findViewById(R.id.rec_arrow1);
            recArrow2 = itemView.findViewById(R.id.rec_arrow2);
            recArrow3 = itemView.findViewById(R.id.rec_arrow3);

        }
    }

}