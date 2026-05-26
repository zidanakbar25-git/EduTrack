// database/MataKuliahDao.java
package com.example.edutrack.database;

import androidx.room.*;
import com.example.edutrack.model.MataKuliah;
import java.util.List;

@Dao
public interface MataKuliahDao {
    @Query("SELECT * FROM mata_kuliah ORDER BY nama ASC")
    List<MataKuliah> getAll();

    @Insert
    void insert(MataKuliah mataKuliah);

    @Update
    void update(MataKuliah mataKuliah);

    @Delete
    void delete(MataKuliah mataKuliah);
}