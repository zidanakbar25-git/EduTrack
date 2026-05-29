package com.example.edutrack.database;

import androidx.room.*;
import com.example.edutrack.model.Tugas;
import java.util.List;

@Dao
public interface TugasDao {

    // === UNTUK DASHBOARD (Orang B) ===

    // Ambil semua tugas aktif, diurutkan deadline terdekat
    @Query("SELECT * FROM tugas WHERE status = 'pending' ORDER BY deadline ASC")
    List<Tugas> getTugasAktif();

    // Hitung total tugas aktif
    @Query("SELECT COUNT(*) FROM tugas WHERE status = 'pending'")
    int countTugasAktif();

    // === UNTUK RIWAYAT (Orang D) ===

    // Ambil semua riwayat
    @Query("SELECT * FROM tugas WHERE status != 'pending' ORDER BY deadline DESC")
    List<Tugas> getTugasRiwayat();

    // Ambil riwayat per matkul
    @Query("SELECT * FROM tugas WHERE status != 'pending' AND mataKuliahId = :matkulId ORDER BY deadline DESC")
    List<Tugas> getRiwayatByMatkul(int matkulId);

    // === UNTUK SEMUA ===

    @Insert
    void insert(Tugas tugas);

    @Update
    void update(Tugas tugas);

    @Delete
    void delete(Tugas tugas);
}