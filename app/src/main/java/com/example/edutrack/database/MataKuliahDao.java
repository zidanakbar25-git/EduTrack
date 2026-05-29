package com.example.edutrack.database;

import androidx.room.*;
import com.example.edutrack.model.MataKuliah;
import java.util.List;

@Dao
public interface MataKuliahDao {

    @Query("SELECT * FROM mata_kuliah ORDER BY nama ASC")
    List<MataKuliah> getAll();

    @Query("SELECT * FROM mata_kuliah WHERE id = :id")
    MataKuliah getById(int id);

    // Cek duplikat nama (case-insensitive)
    @Query("SELECT COUNT(*) FROM mata_kuliah WHERE LOWER(nama) = LOWER(:nama)")
    int countByNama(String nama);

    // Cek duplikat nama saat update (kecualikan id sendiri)
    @Query("SELECT COUNT(*) FROM mata_kuliah WHERE LOWER(nama) = LOWER(:nama) AND id != :excludeId")
    int countByNamaExclude(String nama, int excludeId);

    @Query("SELECT COUNT(*) FROM tugas WHERE mataKuliahId = :matkulId")
    int countTugasByMatkul(int matkulId);

    @Insert
    void insert(MataKuliah mataKuliah);

    @Update
    void update(MataKuliah mataKuliah);

    @Delete
    void delete(MataKuliah mataKuliah);
}
