package com.example.edutrack.ui.matakuliah;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edutrack.R;
import com.example.edutrack.adapter.MataKuliahAdapter;
import com.example.edutrack.database.AppDatabase;
import com.example.edutrack.database.MataKuliahDao;
import com.example.edutrack.model.MataKuliah;

import java.util.ArrayList;
import java.util.List;

public class MataKuliahFragment extends Fragment implements MataKuliahAdapter.OnActionListener {

    private RecyclerView rvMataKuliah;
    private CardView viewEmptyState;
    private Button btnTambahMatkul;

    private AppDatabase db;
    private MataKuliahDao matkulDao;

    private List<MataKuliah> listMatkul;
    private MataKuliahAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_matkul, container, false);

        db        = AppDatabase.getInstance(requireContext());
        matkulDao = db.mataKuliahDao();

        rvMataKuliah  = view.findViewById(R.id.rvMataKuliah);
        viewEmptyState = view.findViewById(R.id.viewEmptyState);
        btnTambahMatkul = view.findViewById(R.id.btnTambahMatkul);

        rvMataKuliah.setLayoutManager(new LinearLayoutManager(getContext()));
        listMatkul = new ArrayList<>();

        refreshDataList();
        btnTambahMatkul.setOnClickListener(v -> tampilkanDialogTambah());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshDataList();
    }

    private void refreshDataList() {
        listMatkul.clear();
        listMatkul.addAll(matkulDao.getAll());

        if (!listMatkul.isEmpty()) {
            viewEmptyState.setVisibility(View.GONE);
            rvMataKuliah.setVisibility(View.VISIBLE);
        } else {
            rvMataKuliah.setVisibility(View.GONE);
            viewEmptyState.setVisibility(View.VISIBLE);
        }

        if (adapter == null) {
            adapter = new MataKuliahAdapter(listMatkul, this);
            rvMataKuliah.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void tampilkanDialogTambah() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tambah_matkul);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        EditText etNama   = dialog.findViewById(R.id.etNamaMatkul);
        Button btnSimpan  = dialog.findViewById(R.id.btnSimpan);
        View   btnClose   = dialog.findViewById(R.id.btnClose);

        btnSimpan.setOnClickListener(v -> {
            String nama = etNama.getText().toString().trim();
            if (nama.isEmpty()) {
                etNama.setError("Nama mata kuliah tidak boleh kosong");
                return;
            }
            if (nama.length() > 100) {
                etNama.setError("Nama maksimal 100 karakter");
                return;
            }
            // Cek duplikat
            if (matkulDao.countByNama(nama) > 0) {
                etNama.setError("Sudah ada mata kuliah dengan nama ini");
                return;
            }

            MataKuliah mk = new MataKuliah();
            mk.nama      = nama;
            mk.createdAt = System.currentTimeMillis();
            matkulDao.insert(mk);

            Toast.makeText(getContext(), "Mata kuliah berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            refreshDataList();
            dialog.dismiss();
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void onUpdate(MataKuliah mataKuliah, String namaBaru) {
        // Cek duplikat (kecualikan diri sendiri)
        if (matkulDao.countByNamaExclude(namaBaru, mataKuliah.id) > 0) {
            Toast.makeText(getContext(),
                    "Sudah ada mata kuliah dengan nama ini", Toast.LENGTH_SHORT).show();
            return;
        }
        mataKuliah.nama = namaBaru;
        matkulDao.update(mataKuliah);
        Toast.makeText(getContext(), "Perubahan berhasil disimpan", Toast.LENGTH_SHORT).show();
        refreshDataList();
    }


    @Override
    public void onDelete(MataKuliah mataKuliah) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_hapus_matkul);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView tvJudul      = dialog.findViewById(R.id.tvJudulHapus);
        Button   btnHapusConf = dialog.findViewById(R.id.btnHapusConfirm);
        Button   btnBatal     = dialog.findViewById(R.id.btnBatal);

        tvJudul.setText("Hapus \"" + mataKuliah.nama + "\"?");

        btnHapusConf.setOnClickListener(v -> {
            matkulDao.delete(mataKuliah);
            Toast.makeText(getContext(), "Mata kuliah dihapus", Toast.LENGTH_SHORT).show();
            refreshDataList();
            dialog.dismiss();
        });

        btnBatal.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
