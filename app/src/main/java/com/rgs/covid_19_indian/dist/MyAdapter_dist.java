package com.rgs.covid_19_indian.dist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rgs.covid_19_indian.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class MyAdapter_dist extends RecyclerView.Adapter<MyAdapter_dist.ViewHolder> {
    private List<Model_dist> listData;
    Context context;

    public void setlist(List<Model_dist> listData) {
        this.listData = listData;
    }

    public MyAdapter_dist(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lidt_data_dist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model_dist ld = listData.get(position);
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat formatter = new DecimalFormat("#,###,###");

        try {
            holder.recViewTotal.setText("Total: "+formatter.format(numberFormat.parse(ld.getConfirmed()).intValue()));
            holder.distnameRec.setText(ld.getKey());
        } catch (ParseException e) {
            e.printStackTrace();
        }



        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        private LinearLayout linearlayoutEvents;
        private TextView distnameRec;
        private RelativeLayout recViewLayoutTotaldist;
        private TextView recViewTotal;


        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearlayout_events);
            statenameRec = itemView.findViewById(R.id.statename_rec);


            cardView = itemView.findViewById(R.id.card_view);
            linearlayoutEvents = itemView.findViewById(R.id.linearlayout_events);
            distnameRec = itemView.findViewById(R.id.distname_rec);
            recViewLayoutTotaldist = itemView.findViewById(R.id.rec_view_layout_total_dist);
            recViewTotal = itemView.findViewById(R.id.rec_view_total);

            cardView = itemView.findViewById(R.id.card_view);


        }
    }

}