package com.example.edutrack.database;

import androidx.room.*;
import com.example.edutrack.model.MataKuliah;
import java.util.List;

@Dao
public interface MataKuliahDao {

    @Query("SELECT * FROM mata_kuliah ORDER BY nama ASC")
    List<MataKuliah> getAll();

    // Tambahan: ambil matkul by ID → dipakai Orang B & D
    @Query("SELECT * FROM mata_kuliah WHERE id = :id")
    MataKuliah getById(int id);

    // Tambahan: cek apakah matkul punya tugas → dipakai Orang C sebelum hapus
    @Query("SELECT COUNT(*) FROM tugas WHERE mataKuliahId = :matkulId")
    int countTugasByMatkul(int matkulId);

    @Insert
    void insert(MataKuliah mataKuliah);

    @Update
    void update(MataKuliah mataKuliah);

    @Delete
    void delete(MataKuliah mataKuliah);
}