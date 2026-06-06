package com.example.edutrack.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edutrack.R;
import com.example.edutrack.model.Tugas;
import com.example.edutrack.util.DateHelper;

import java.util.List;

public class RiwayatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM   = 1;

    private final List<Object> items;

    public RiwayatAdapter(List<Object> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return (items.get(position) instanceof String) ? TYPE_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HEADER) {
            View v = inf.inflate(R.layout.item_riwayat_header, parent, false);
            return new HeaderViewHolder(v);
        } else {
            View v = inf.inflate(R.layout.item_riwayat_tugas, parent, false);
            return new ItemViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            String namaMk = (String) items.get(position);
            ((HeaderViewHolder) holder).tvNamaMk.setText(namaMk);
        } else {
            Tugas t = (Tugas) items.get(position);
            bindItem((ItemViewHolder) holder, t);
        }
    }

    private void bindItem(ItemViewHolder h, Tugas t) {
        h.tvJudul.setText(t.judul);

        if (t.deskripsi != null && !t.deskripsi.isEmpty()) {
            h.tvCatatan.setText(t.deskripsi);
            h.tvCatatan.setVisibility(View.VISIBLE);
        } else {
            h.tvCatatan.setVisibility(View.GONE);
        }

        h.tvDeadline.setText("Deadline: " + DateHelper.formatTanggal(t.deadline));

        setBadgeKategori(h.tvBadge, t.jenisTugas);

        boolean isSelesai = "selesai".equals(t.status);
        if (isSelesai) {
            h.tvLabelStatus.setText("SELESAI");
            h.tvLabelStatus.setTextColor(Color.parseColor("#166534"));
            h.tvLabelStatus.setBackgroundResource(R.drawable.bg_status_selesai);
        } else {
            h.tvLabelStatus.setText("GAGAL DIKERJAKAN");
            h.tvLabelStatus.setTextColor(Color.parseColor("#991B1B"));
            h.tvLabelStatus.setBackgroundResource(R.drawable.bg_status_gagal);
        }
    }

    private void setBadgeKategori(TextView badge, String kategori) {
        if (kategori == null) return;
        switch (kategori) {
            case "Tugas Praktikum":
                badge.setText("Tugas Praktikum");
                badge.setBackgroundColor(Color.parseColor("#D1ECF1"));
                badge.setTextColor(Color.parseColor("#0C5460"));
                break;
            case "Tugas Besar":
                badge.setText("Tugas Besar");
                badge.setBackgroundColor(Color.parseColor("#F8D7DA"));
                badge.setTextColor(Color.parseColor("#721C24"));
                break;
            default:
                badge.setText("Tugas Biasa");
                badge.setBackgroundColor(Color.parseColor("#FFF3CD"));
                badge.setTextColor(Color.parseColor("#856404"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaMk;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaMk = itemView.findViewById(R.id.tvNamaMatkulHeader);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudul, tvCatatan, tvDeadline, tvBadge, tvLabelStatus;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul       = itemView.findViewById(R.id.tvJudulRiwayat);
            tvCatatan     = itemView.findViewById(R.id.tvCatatanRiwayat);
            tvDeadline    = itemView.findViewById(R.id.tvDeadlineRiwayat);
            tvBadge       = itemView.findViewById(R.id.tvBadgeKategoriRiwayat);
            tvLabelStatus = itemView.findViewById(R.id.tvLabelStatus);
        }
    }
}