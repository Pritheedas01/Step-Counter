package com.pritha.www.stepcount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterData extends RecyclerView.Adapter<AdapterData.MyHolder> {
    Context context;
    List<ModelData> list;

    public AdapterData( Context context,List<ModelData> list) {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.data,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final ModelData data=list.get(position);
        String Txt_date=list.get(position).getDate();
        String Txt_steps=list.get(position).getSteps();
        String Txt_Kms=list.get(position).getKMs();
        String Txt_Calories=list.get(position).getCalorie();
//        holder.Date.setText(Txt_date);
        holder.Steps.setText(Txt_steps);
        holder.Kms.setText("KMS:-" + Txt_Kms);
        holder.Calories.setText("KALs:-" + Txt_Calories);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        TextView Date,Steps,Kms,Calories;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            Steps=itemView.findViewById(R.id.steps);
            Kms=itemView.findViewById(R.id.KMs);
            Calories=itemView.findViewById(R.id.calories);
        }
    }
}