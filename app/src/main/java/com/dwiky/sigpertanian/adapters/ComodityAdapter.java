package com.dwiky.sigpertanian.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dwiky.sigpertanian.R;
import com.dwiky.sigpertanian.databinding.ComodityItemListBinding;
import com.dwiky.sigpertanian.models.Comoditas;

import java.util.List;

public class ComodityAdapter extends RecyclerView.Adapter<ComodityAdapter.ViewHolder> {
    private Context context;
    private List<Comoditas> comodities;
    private OnClickComodityAdapter listener;

    public ComodityAdapter(Context context, List<Comoditas> comodities, OnClickComodityAdapter listener) {
        this.context = context;
        this.comodities = comodities;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ComodityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View  v = inflater.inflate(R.layout.comodity_item_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ComodityAdapter.ViewHolder holder, int position) {
        Comoditas comodity = comodities.get(position);
        boolean isExpanded = comodities.get(position).isExpanded();

        holder.tvNamaKomoditas.setText(comodity.getNamakomoditas());
        holder.tvJumlah.setText(comodity.getJumlah());
        holder.tvAwal.setText(comodity.getAwal());
        holder.tvAkhir.setText(comodity.getAkhir());
        holder.tvDesa.setText(comodity.getDesa());
        holder.tvKecamatan.setText(comodity.getKecamatan());

        holder.containerBody.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.chevron.setRotation(isExpanded ? 90 : -90);

    }

    @Override
    public int getItemCount() {
        return comodities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaKomoditas, tvJumlah, tvAwal, tvAkhir, tvDesa, tvKecamatan;
        ConstraintLayout containerBody, containerTitle;
        ImageView chevron;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNamaKomoditas = itemView.findViewById(R.id.tvNamaKomoditas);
            tvJumlah = itemView.findViewById(R.id.tvJumlah);
            tvAwal = itemView.findViewById(R.id.tvAwal);
            tvAkhir = itemView.findViewById(R.id.tvAkhir);
            tvDesa = itemView.findViewById(R.id.tvDesa);
            tvKecamatan = itemView.findViewById(R.id.tvKecamatan);
            chevron = itemView.findViewById(R.id.chevron);
            containerTitle = itemView.findViewById(R.id.title_container);
            containerBody = itemView.findViewById(R.id.body_container);

            chevron.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Comoditas comodity = comodities.get(getAdapterPosition());
                    comodity.setExpanded(!comodity.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

            containerTitle.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(context)
                            .setTitle("Perhatian")
                            .setMessage("Pilih Operasi Yang Akan Dilakukan")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Edit", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    listener.edit(comodities.get(getAdapterPosition()));

                                }})
                            .setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    listener.delete(comodities.get(getAdapterPosition()));
                                }
                            }).show();
                    return false;
                }
            });
        }
    }
}

