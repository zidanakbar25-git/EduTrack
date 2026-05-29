package com.example.edutrack.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edutrack.R;
import com.example.edutrack.model.MataKuliah;
import com.example.edutrack.model.Tugas;
import com.example.edutrack.util.DateHelper;

import java.util.List;
import java.util.Map;

public class TugasAdapter extends RecyclerView.Adapter<TugasAdapter.ViewHolder> {

    private final List<Tugas> listTugas;
    private final Map<Integer, String> mapMatkul; // id -> nama
    private final OnTugasActionListener listener;

    public interface OnTugasActionListener {
        void onSelesai(Tugas tugas);
        void onGagal(Tugas tugas);
    }

    public TugasAdapter(List<Tugas> listTugas, Map<Integer, String> mapMatkul,
                        OnTugasActionListener listener) {
        this.listTugas  = listTugas;
        this.mapMatkul  = mapMatkul;
        this.listener   = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tugas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Tugas t = listTugas.get(position);

        // Judul
        h.tvJudul.setText(t.judul);

        // Mata Kuliah
        String namaMk = mapMatkul.containsKey(t.mataKuliahId)
                ? mapMatkul.get(t.mataKuliahId) : "-";
        h.tvMatkul.setText(namaMk);

        // Catatan
        if (t.deskripsi != null && !t.deskripsi.isEmpty()) {
            h.tvCatatan.setText(t.deskripsi);
            h.tvCatatan.setVisibility(View.VISIBLE);
        } else {
            h.tvCatatan.setVisibility(View.GONE);
        }

        // Deadline
        h.tvDeadline.setText(DateHelper.formatTanggal(t.deadline));

        // Sisa hari
        String labelSisa = DateHelper.labelSisaHari(t.deadline);
        String warnaSisa = DateHelper.warnaSisaHari(t.deadline);
        h.tvSisaHari.setText(labelSisa);
        h.tvSisaHari.setTextColor(Color.parseColor(warnaSisa));

        // Badge kategori
        setBadgeKategori(h.tvBadge, t.jenisTugas);

        // Tombol aksi
        h.btnSelesai.setOnClickListener(v -> listener.onSelesai(t));
        h.btnGagal.setOnClickListener(v  -> listener.onGagal(t));
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
            default: // Tugas Biasa
                badge.setText("Tugas Biasa");
                badge.setBackgroundColor(Color.parseColor("#FFF3CD"));
                badge.setTextColor(Color.parseColor("#856404"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return listTugas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudul, tvMatkul, tvCatatan, tvDeadline, tvSisaHari, tvBadge;
        Button btnSelesai, btnGagal;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul    = itemView.findViewById(R.id.tvJudulTugas);
            tvMatkul   = itemView.findViewById(R.id.tvNamaMatkul);
            tvCatatan  = itemView.findViewById(R.id.tvCatatan);
            tvDeadline = itemView.findViewById(R.id.tvDeadline);
            tvSisaHari = itemView.findViewById(R.id.tvSisaHari);
            tvBadge    = itemView.findViewById(R.id.tvBadgeKategori);
            btnSelesai = itemView.findViewById(R.id.btnSelesai);
            btnGagal   = itemView.findViewById(R.id.btnGagal);
        }
    }
}
