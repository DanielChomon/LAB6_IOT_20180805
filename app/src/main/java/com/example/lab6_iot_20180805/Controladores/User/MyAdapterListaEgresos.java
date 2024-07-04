package com.example.lab6_iot_20180805.Controladores.User;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6_iot_20180805.R;

import java.util.List;

public class MyAdapterListaEgresos extends RecyclerView.Adapter<MyViewHolderEgresos> {

    private Context context;
    private List<engresoDataClass> dataList;

    public void setSearchList_sitios(List<engresoDataClass> dataSearchList){
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }

    public MyAdapterListaEgresos(Context context, List<engresoDataClass> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolderEgresos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_egreso, parent, false);
        return new MyViewHolderEgresos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderEgresos holder, int position) {
        try {
            engresoDataClass item = dataList.get(position);
            holder.rec_nombre.setText(item.getNombre());
            holder.rec_distrito.setText(item.getDistrito());
            holder.rec_numSuper.setText(item.getNumSuper());

            holder.recCard.setOnClickListener(v -> {
                Intent intent = new Intent(context, admin_info_sitio.class);
                intent.putExtra("Nombre", item.getNombre());
                intent.putExtra("Distrito", item.getDistrito());
                intent.putExtra("NumSuper", item.getNumSuper());
                intent.putExtra("Codigo", item.getCodigo());
                intent.putExtra("Departamento", item.getDepartamento());
                intent.putExtra("Provincia", item.getProvincia());
                intent.putExtra("Longitud", item.getLongitud());
                intent.putExtra("Latitud", item.getLatitud());
                intent.putExtra("Ubigeo", item.getUbigeo());
                intent.putExtra("Tip_zona", item.getTipodezona());
                intent.putExtra("Tip_sitio", item.getTipodesitio());

                context.startActivity(intent);
            });
        } catch (Exception e) {
            Log.e("AdapterError", "Error at position " + position + ": " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }
}

class MyViewHolderEgresos extends RecyclerView.ViewHolder{
    TextView rec_nombre, rec_numSuper, rec_distrito;
    CardView recCard;

    public MyViewHolderEgresos(@NonNull View itemView){
        super(itemView);

        rec_nombre = itemView.findViewById(R.id.textView_nameSitio_cardRC_admin);
        rec_distrito = itemView.findViewById(R.id.textView_distrito_cardRC_admin);
        rec_numSuper = itemView.findViewById(R.id.textView_numSuper_cardRC_admin);
        recCard = itemView.findViewById(R.id.recCard_item_listsitios_admin);
    }
}

