package com.example.edutrack.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.edutrack.R;
import com.example.edutrack.model.MataKuliah;
import java.util.List;

public class MataKuliahAdapter extends RecyclerView.Adapter<MataKuliahAdapter.ViewHolder> {

    private final List<MataKuliah> listMatkul;
    private final OnActionListener listener;

    public interface OnActionListener {
        void onUpdate(MataKuliah mataKuliah, String namaBaru);
        void onDelete(MataKuliah mataKuliah);
    }

    public MataKuliahAdapter(List<MataKuliah> listMatkul, OnActionListener listener) {
        this.listMatkul = listMatkul;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mata_kuliah, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MataKuliah matkul = listMatkul.get(position);
        final String namaLama = matkul.nama;

        holder.tvNamaMatkul.setText(namaLama);
        holder.etNamaMatkulEdit.setText(namaLama);

        // FIX UTAMA: Setiap kali view di-bind ulang oleh refreshDataList(),
        // kembalikan paksa visibilitas ke mode normal agar tidak stuck di mode edit!
        holder.layoutItemNormal.setVisibility(View.VISIBLE);
        holder.layoutInlineEdit.setVisibility(View.GONE);

        // Aksi Tombol Edit
        holder.btnEdit.setOnClickListener(v -> {
            holder.layoutItemNormal.setVisibility(View.GONE);
            holder.layoutInlineEdit.setVisibility(View.VISIBLE);
        });

        // Aksi Tombol Batal
        holder.btnInlineBatal.setOnClickListener(v -> {
            holder.layoutInlineEdit.setVisibility(View.GONE);
            holder.layoutItemNormal.setVisibility(View.VISIBLE);
            holder.etNamaMatkulEdit.setText(namaLama); // Reset teks ke bawaan awal
        });

        // Aksi Tombol OK
        holder.btnInlineOk.setOnClickListener(v -> {
            String namaBaru = holder.etNamaMatkulEdit.getText().toString().trim();
            if (!namaBaru.isEmpty()) {
                // Lempar aksi ke fragment untuk update DB & trigger refreshDataList()
                listener.onUpdate(matkul, namaBaru);
            } else {
                holder.etNamaMatkulEdit.setError("Tidak boleh kosong!");
            }
        });

        // Aksi Tombol Hapus
        holder.btnHapus.setOnClickListener(v -> listener.onDelete(matkul));
    }

    @Override
    public int getItemCount() {
        return listMatkul.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaMatkul;
        EditText etNamaMatkulEdit;
        ImageButton btnEdit, btnHapus;
        Button btnInlineOk, btnInlineBatal;
        RelativeLayout layoutItemNormal;
        View layoutInlineEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaMatkul = itemView.findViewById(R.id.tvNamaMatkul);
            etNamaMatkulEdit = itemView.findViewById(R.id.etNamaMatkulEdit);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnHapus = itemView.findViewById(R.id.btnHapus);
            btnInlineOk = itemView.findViewById(R.id.btnInlineOk);
            btnInlineBatal = itemView.findViewById(R.id.btnInlineBatal);
            layoutItemNormal = itemView.findViewById(R.id.layoutItemNormal);
            layoutInlineEdit = itemView.findViewById(R.id.layoutInlineEdit);
        }
    }
}