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

    // Room Components
    private AppDatabase db;
    private MataKuliahDao matkulDao;

    private List<MataKuliah> listMatkul;
    private MataKuliahAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Menggunakan activity_matkul.xml sesuai dengan struktur layout yang Anda miliki
        View view = inflater.inflate(R.layout.activity_matkul, container, false);

        // 1. Inisialisasi Komponen Room Database
        db = AppDatabase.getInstance(requireContext());
        matkulDao = db.mataKuliahDao();

        // 2. Inisialisasi View Komponen UI
        rvMataKuliah = view.findViewById(R.id.rvMataKuliah);
        viewEmptyState = view.findViewById(R.id.viewEmptyState);
        btnTambahMatkul = view.findViewById(R.id.btnTambahMatkul);

        rvMataKuliah.setLayoutManager(new LinearLayoutManager(getContext()));
        listMatkul = new ArrayList<>();

        // 3. Muat Data List Pertama Kali
        refreshDataList();

        // 4. Trigger Tombol Tambah Mata Kuliah
        btnTambahMatkul.setOnClickListener(v -> tampilkanDialogTambah());

        return view;
    }

    // Fungsi Pengambil Data Menggunakan Kueri Room "getAll()"
    private void refreshDataList() {
        listMatkul.clear();
        // Memanggil kueri SELECT * FROM mata_kuliah ORDER BY nama ASC dari DAO
        listMatkul.addAll(matkulDao.getAll());

        if (!listMatkul.isEmpty()) {
            viewEmptyState.setVisibility(View.GONE);
            rvMataKuliah.setVisibility(View.VISIBLE);
        } else {
            rvMataKuliah.setVisibility(View.GONE);
            viewEmptyState.setVisibility(View.VISIBLE);
        }

        // Sinkronisasi Data ke Adapter
        if (adapter == null) {
            adapter = new MataKuliahAdapter(listMatkul, this);
            rvMataKuliah.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    // Aksi Operasi Tambah Data (Insert) via Floating Dialog Component
    private void tampilkanDialogTambah() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tambah_matkul);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        EditText etNamaMatkul = dialog.findViewById(R.id.etNamaMatkul);
        Button btnSimpan = dialog.findViewById(R.id.btnSimpan);
        View btnClose = dialog.findViewById(R.id.btnClose);

        btnSimpan.setOnClickListener(v -> {
            String namaInput = etNamaMatkul.getText().toString().trim();
            if (!namaInput.isEmpty()) {
                // Inisialisasi Objek Model Room
                MataKuliah matkulBaru = new MataKuliah();

                // PERBAIKAN: Set langsung ke variabel publik .nama sesuai model terbarumu
                matkulBaru.nama = namaInput;

                // Eksekusi fungsi @Insert milik Dao
                matkulDao.insert(matkulBaru);

                Toast.makeText(getContext(), "Mata kuliah berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                refreshDataList();
                dialog.dismiss();
            } else {
                etNamaMatkul.setError("Nama tidak boleh kosong!");
            }
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    // Aksi Callback Operasi Pembaruan Data (Update)
    @Override
    public void onUpdate(MataKuliah mataKuliah, String namaBaru) {
        // 1. Perbarui nama di objek model
        mataKuliah.nama = namaBaru;

        // 2. Eksekusi pembaruan ke database Room
        matkulDao.update(mataKuliah);

        // 3. Tampilkan Notifikasi Sukses sesuai permintaanmu
        Toast.makeText(getContext(), "Perubahan Berhasil Disimpan", Toast.LENGTH_SHORT).show();

        // 4. Muat ulang data list dari database agar urutan ORDER BY nama ASC tetap konsisten
        refreshDataList();
    }

    // Aksi Callback Operasi Penghapusan Data (Delete) dengan Proteksi Relasi Tugas
    @Override
    public void onDelete(MataKuliah mataKuliah) {
        // PERBAIKAN: Menggunakan variabel publik .id sesuai model terbarumu
        int jumlahTugasTerkait = matkulDao.countTugasByMatkul(mataKuliah.id);

        if (jumlahTugasTerkait > 0) {
            // Jika mata kuliah ini masih memiliki relasi data di tabel tugas, tolak proses hapus!
            Toast.makeText(getContext(), "Gagal menghapus! Masih ada " + jumlahTugasTerkait + " tugas aktif di mata kuliah ini.", Toast.LENGTH_LONG).show();
            return;
        }

        // JIKA AMAN: Jalankan Pop-up Dialog Konfirmasi Hapus seperti gambar UI-mu
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_hapus_matkul);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView tvJudulHapus = dialog.findViewById(R.id.tvJudulHapus);
        Button btnHapusConfirm = dialog.findViewById(R.id.btnHapusConfirm);
        Button btnBatal = dialog.findViewById(R.id.btnBatal);

        // PERBAIKAN: Menggunakan variabel publik .nama sesuai model terbarumu
        tvJudulHapus.setText("Hapus \"" + mataKuliah.nama + "\"?");

        btnHapusConfirm.setOnClickListener(v -> {
            // Eksekusi fungsi @Delete milik Dao
            matkulDao.delete(mataKuliah);

            Toast.makeText(getContext(), "Mata kuliah berhasil deleted", Toast.LENGTH_SHORT).show();
            refreshDataList();
            dialog.dismiss();
        });

        btnBatal.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}