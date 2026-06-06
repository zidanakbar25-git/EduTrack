package com.example.edutrack.ui.dashboard;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edutrack.R;
import com.example.edutrack.adapter.TugasAdapter;
import com.example.edutrack.database.AppDatabase;
import com.example.edutrack.database.MataKuliahDao;
import com.example.edutrack.database.TugasDao;
import com.example.edutrack.model.MataKuliah;
import com.example.edutrack.model.Tugas;
import com.example.edutrack.util.DateHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class DashboardFragment extends Fragment implements TugasAdapter.OnTugasActionListener {

    private RecyclerView rvTugas;
    private TextView tvTotalTugas;
    private LinearLayout layoutEmpty;

    private AppDatabase db;
    private TugasDao tugasDao;
    private MataKuliahDao matkulDao;

    private List<Tugas> listTugas;
    private TugasAdapter adapter;

    // State untuk dialog tambah tugas
    private int selectedYear = -1, selectedMonth = -1, selectedDay = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        db        = AppDatabase.getInstance(requireContext());
        tugasDao  = db.tugasDao();
        matkulDao = db.mataKuliahDao();

        rvTugas      = view.findViewById(R.id.rvTugas);
        tvTotalTugas = view.findViewById(R.id.tvTotalTugas);
        layoutEmpty  = view.findViewById(R.id.layoutEmptyDashboard);

        rvTugas.setLayoutManager(new LinearLayoutManager(getContext()));
        listTugas = new ArrayList<>();

        Button btnTambah = view.findViewById(R.id.btnTambahTugas);
        btnTambah.setOnClickListener(v -> tampilkanDialogTambahTugas());

        // Auto-mark tugas terlambat sebelum refresh
        autoMarkGagal();
        refreshData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        autoMarkGagal();
        refreshData();
    }

    // Tandai tugas pending yang deadline-nya sudah lewat sebagai 'gagal'
    private void autoMarkGagal() {
        long now = System.currentTimeMillis();
        List<Tugas> terlambat = tugasDao.getPendingTerlambat(now);
        for (Tugas t : terlambat) {
            t.status = "gagal";
            tugasDao.update(t);
        }
    }

    private void refreshData() {
        listTugas.clear();
        listTugas.addAll(tugasDao.getTugasAktif());

        int total = listTugas.size();
        tvTotalTugas.setText(String.valueOf(total));

        if (total == 0) {
            layoutEmpty.setVisibility(View.VISIBLE);
            rvTugas.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            rvTugas.setVisibility(View.VISIBLE);
        }

        // Buat map id -> nama matkul untuk adapter
        Map<Integer, String> mapMatkul = new HashMap<>();
        for (MataKuliah mk : matkulDao.getAll()) {
            mapMatkul.put(mk.id, mk.nama);
        }

        if (adapter == null) {
            adapter = new TugasAdapter(listTugas, mapMatkul, this);
            rvTugas.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    // === Dialog Tambah Tugas ===
    private void tampilkanDialogTambahTugas() {
        List<MataKuliah> listMatkul = matkulDao.getAll();
        if (listMatkul.isEmpty()) {
            Toast.makeText(getContext(),
                    "Tambahkan mata kuliah terlebih dahulu!", Toast.LENGTH_SHORT).show();
            return;
        }

        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tambah_tugas);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.92f),
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        // Reset state deadline
        selectedYear = selectedMonth = selectedDay = -1;

        EditText etJudul   = dialog.findViewById(R.id.etJudulTugas);
        EditText etCatatan = dialog.findViewById(R.id.etCatatan);
        Spinner  spKat     = dialog.findViewById(R.id.spinnerKategori);
        Spinner  spMatkul  = dialog.findViewById(R.id.spinnerMatkul);
        LinearLayout layoutDeadline = dialog.findViewById(R.id.layoutDeadline);
        TextView tvDeadline = dialog.findViewById(R.id.tvSelectedDeadline);
        Button btnSimpan   = dialog.findViewById(R.id.btnSimpanTugas);
        TextView btnClose  = dialog.findViewById(R.id.btnCloseTugas);

        // Spinner Kategori
        String[] kategoriArr = {"tugas biasa", "Tugas Praktikum", "Tugas Besar"};
        ArrayAdapter<String> katAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, kategoriArr);
        katAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKat.setAdapter(katAdapter);

        // Spinner Mata Kuliah
        List<String> namaMkList = new ArrayList<>();
        namaMkList.add("Pilih mata kuliah");
        for (MataKuliah mk : listMatkul) namaMkList.add(mk.nama);

        ArrayAdapter<String> mkAdapter = new ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_spinner_item, namaMkList) {

            // Item pertama tidak bisa diklik dan warnanya redup
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.parseColor("#2B2B2B")); // redup
                } else {
                    tv.setTextColor(Color.parseColor("#BDBDBD")); // normal
                }
                return view;
            }
        };
        mkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMatkul.setAdapter(mkAdapter);

        // DatePicker
        layoutDeadline.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"));
            int y = cal.get(Calendar.YEAR);
            int m = cal.get(Calendar.MONTH);
            int d = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(requireContext(),
                    (picker, year, month, dayOfMonth) -> {
                        if (DateHelper.isDeadlineValid(year, month, dayOfMonth)) {
                            selectedYear  = year;
                            selectedMonth = month;
                            selectedDay   = dayOfMonth;
                            // Format tampilan
                            tvDeadline.setText(DateHelper.formatTanggal(
                                    DateHelper.toDeadlineMillis(year, month, dayOfMonth)));
                            tvDeadline.setTextColor(Color.parseColor("#2B2B2B"));
                        } else {
                            Toast.makeText(getContext(),
                                    "Deadline tidak boleh sebelum hari ini", Toast.LENGTH_SHORT).show();
                        }
                    }, y, m, d);
            // Minimal date = hari ini
            dpd.getDatePicker().setMinDate(DateHelper.todayStartMillis());
            dpd.show();
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());

        btnSimpan.setOnClickListener(v -> {
            String judul   = etJudul.getText().toString().trim();
            String catatan = etCatatan.getText().toString().trim();
            int posKat     = spKat.getSelectedItemPosition();
            int posMk      = spMatkul.getSelectedItemPosition();

            // Validasi
            if (judul.isEmpty()) {
                etJudul.setError("Judul tugas tidak boleh kosong");
                return;
            }
            if (selectedYear == -1) {
                Toast.makeText(getContext(), "Pilih tanggal deadline", Toast.LENGTH_SHORT).show();
                return;
            }
            if (posMk == 0) {
                Toast.makeText(getContext(), "Pilih mata kuliah terlebih dahulu", Toast.LENGTH_SHORT).show();
                return;
            }

            // Map kategori
            String[] kategoriValues = {"Tugas Biasa", "Tugas Praktikum", "Tugas Besar"};
            String kategori = kategoriValues[posKat];

            MataKuliah mkTerpilih = listMatkul.get(posMk - 1); // offset 1 karena "Pilih..."

            Tugas tugas = new Tugas();
            tugas.judul       = judul;
            tugas.deskripsi   = catatan.isEmpty() ? null : catatan;
            tugas.mataKuliahId = mkTerpilih.id;
            tugas.jenisTugas  = kategori;
            tugas.deadline    = DateHelper.toDeadlineMillis(selectedYear, selectedMonth, selectedDay);
            tugas.status      = "pending";
            tugas.createdAt   = System.currentTimeMillis();

            tugasDao.insert(tugas);
            Toast.makeText(getContext(), "Tugas berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            refreshData();
        });

        dialog.show();
    }

    // === Callback dari TugasAdapter ===

    @Override
    public void onSelesai(Tugas tugas) {
        tugas.status    = "selesai";
        tugas.selesaiAt = System.currentTimeMillis();
        tugasDao.update(tugas);
        Toast.makeText(getContext(), "Tugas ditandai selesai ✓", Toast.LENGTH_SHORT).show();
        refreshData();
    }

    @Override
    public void onGagal(Tugas tugas) {
        tugas.status = "gagal";
        tugasDao.update(tugas);
        Toast.makeText(getContext(), "Tugas ditandai gagal", Toast.LENGTH_SHORT).show();
        refreshData();
    }
}
