package com.dwiky.sigpertanian.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dwiky.sigpertanian.R;
import com.dwiky.sigpertanian.models.Agriculture;
import com.dwiky.sigpertanian.ui.activities.AgricultureDetailActivity;

import java.util.List;

public class AgricultureAdapter extends RecyclerView.Adapter<AgricultureAdapter.ViewHolder> {
    private Context context;
    private List<Agriculture> agricultures;
    private OnClickAgricultureAdapter listener;

    public AgricultureAdapter(Context context, List<Agriculture> agricultures, OnClickAgricultureAdapter listener){
        this.context = context;
        this.agricultures = agricultures;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.agriculture_item_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Agriculture agriculture = agricultures.get(position);

        holder.tvNamaPemilik.setText(agriculture.getNamapemilik());
        holder.tvLuasMeter.setText(agriculture.getLuas() + " | " + agriculture.getMeter());
        holder.tvDesaKecamatan.setText(agriculture.getDesa()+ " | "+ agriculture.getKecamatan());
        Glide.with(context)
                .load(agriculture.getFoto())
                .into(holder.ivFoto);

        holder.btnDetail.setOnClickListener(view -> {
            Intent intent = new Intent(context, AgricultureDetailActivity.class);
            intent.putExtra("ID_LAHAN", agriculture.getId_lahan().toString());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return agricultures.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaPemilik, tvLuasMeter, tvDesaKecamatan;
        ImageView ivFoto;
        Button btnDetail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaPemilik = itemView.findViewById(R.id.nama_pemilik);
            tvLuasMeter = itemView.findViewById(R.id.luas_meter);
            tvDesaKecamatan = itemView.findViewById(R.id.desa_kecamatan);
            ivFoto = itemView.findViewById(R.id.ivFoto);
            btnDetail = itemView.findViewById(R.id.btnDetail);



        }
    }
}
