package com.example.edutrack.ui.riwayat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edutrack.R;
import com.example.edutrack.adapter.RiwayatAdapter;
import com.example.edutrack.database.AppDatabase;
import com.example.edutrack.database.MataKuliahDao;
import com.example.edutrack.database.TugasDao;
import com.example.edutrack.model.MataKuliah;
import com.example.edutrack.model.Tugas;

import java.util.ArrayList;
import java.util.List;

public class RiwayatFragment extends Fragment {

    private RecyclerView rvRiwayat;
    private LinearLayout layoutEmpty;

    private AppDatabase db;
    private TugasDao tugasDao;
    private MataKuliahDao matkulDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_riwayat, container, false);

        db        = AppDatabase.getInstance(requireContext());
        tugasDao  = db.tugasDao();
        matkulDao = db.mataKuliahDao();

        rvRiwayat   = view.findViewById(R.id.rvRiwayat);
        layoutEmpty = view.findViewById(R.id.layoutEmptyRiwayat);

        rvRiwayat.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        // Ambil semua matkul
        List<MataKuliah> listMatkul = matkulDao.getAll();

        // Bangun flat list: [Header String, Tugas, Tugas, Header, Tugas, ...]
        List<Object> items = new ArrayList<>();

        for (MataKuliah mk : listMatkul) {
            // Ambil semua tugas di matkul ini yang sudah selesai atau gagal
            List<Tugas> semuaTugas = tugasDao.getAllByMatkul(mk.id);

            List<Tugas> riwayatMk = new ArrayList<>();
            for (Tugas t : semuaTugas) {
                // Tampilkan hanya yang status != pending
                // (tugas pending yang terlambat sudah di-auto-mark 'gagal' oleh Dashboard)
                if (!"pending".equals(t.status)) {
                    riwayatMk.add(t);
                }
            }

            if (!riwayatMk.isEmpty()) {
                items.add(mk.nama); // header
                items.addAll(riwayatMk);
            }
        }

        if (items.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            rvRiwayat.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            rvRiwayat.setVisibility(View.VISIBLE);
            rvRiwayat.setAdapter(new RiwayatAdapter(items));
        }
    }
}
