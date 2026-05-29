package com.example.edutrack.database;

import androidx.room.*;
import com.example.edutrack.model.Tugas;
import java.util.List;

@Dao
public interface TugasDao {

    // === DASHBOARD: tugas aktif (pending), ascending deadline ===
    @Query("SELECT * FROM tugas WHERE status = 'pending' ORDER BY deadline ASC")
    List<Tugas> getTugasAktif();

    @Query("SELECT COUNT(*) FROM tugas WHERE status = 'pending'")
    int countTugasAktif();

    // === RIWAYAT: tugas yang sudah selesai atau gagal ===
    // Tugas 'selesai' = user tandai selesai
    // Tugas 'gagal'   = masih pending tapi deadline sudah lewat → di-handle di Fragment
    @Query("SELECT * FROM tugas WHERE status != 'pending' ORDER BY deadline DESC")
    List<Tugas> getTugasRiwayat();

    // Semua tugas per matkul (untuk riwayat grouped)
    @Query("SELECT * FROM tugas WHERE mataKuliahId = :matkulId ORDER BY deadline DESC")
    List<Tugas> getAllByMatkul(int matkulId);

    // Tugas pending yang deadline-nya sudah lewat (auto-gagal logic)
    @Query("SELECT * FROM tugas WHERE status = 'pending' AND deadline < :nowMillis")
    List<Tugas> getPendingTerlambat(long nowMillis);

    // Hitung tugas aktif per matkul (untuk proteksi hapus matkul)
    @Query("SELECT COUNT(*) FROM tugas WHERE mataKuliahId = :matkulId AND status = 'pending'")
    int countAktifByMatkul(int matkulId);

    @Insert
    void insert(Tugas tugas);

    @Update
    void update(Tugas tugas);

    @Delete
    void delete(Tugas tugas);
}
