// database/TugasDao.java
package com.example.edutrack.database;

import androidx.room.*;
import com.example.edutrack.model.Tugas;
import java.util.List;

@Dao
public interface TugasDao {
    // Tugas aktif (pending), diurutkan dari deadline terdekat
    @Query("SELECT * FROM tugas WHERE status = 'pending' ORDER BY deadline ASC")
    List<Tugas> getTugasAktif();

    // Tugas riwayat (selesai atau gagal)
    @Query("SELECT * FROM tugas WHERE status != 'pending' ORDER BY deadline DESC")
    List<Tugas> getTugasRiwayat();

    @Insert
    void insert(Tugas tugas);

    @Update
    void update(Tugas tugas);

    @Delete
    void delete(Tugas tugas);
}