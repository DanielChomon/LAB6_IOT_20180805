package com.example.lab6_iot_20180805.Controladores.User;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lab6_iot_20180805.R;
import com.example.lab6_iot_20180805.entity.Ingreso;

import java.util.List;

public class MyAdapterListaIngresos extends RecyclerView.Adapter<MyViewHolderIngresos> {

    private Context context;
    private List<Ingreso> dataList;

    public void setSearchList(List<Ingreso> dataSearchList){
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }

    public MyAdapterListaIngresos(Context context, List<Ingreso> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolderIngresos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_ingreso_2, parent, false);
        return new MyViewHolderIngresos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderIngresos holder, int position) {

        Glide.with(context).load(dataList.get(position).getImagen()).into(holder.recImage);
        holder.rec_nombre.setText(dataList.get(position).getNombre() + " " +dataList.get(position).getApellido());

        holder.rec_status.setText(dataList.get(position).getEstado());
        holder.rec_numSites.setText("3 ");

        //aqui le asigna los valores que aparecen en el recycle view

    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }
}

class MyViewHolderIngresos extends RecyclerView.ViewHolder{
    ImageView recImage;
    TextView rec_nombre, rec_numSites, rec_status;
    CardView recCard;

    public MyViewHolderIngresos(@NonNull View itemView){
        super(itemView);

        recImage = itemView.findViewById(R.id.image_itemSuperList_admin);
        rec_nombre = itemView.findViewById(R.id.textView_name_cardRC_admin);
        rec_status = itemView.findViewById(R.id.textView_status_cardRC_admin);
        rec_numSites = itemView.findViewById(R.id.textView_numbersite_cardRC_admin);
        recCard = itemView.findViewById(R.id.recCard_item_listsuper_admin);
    }
}

