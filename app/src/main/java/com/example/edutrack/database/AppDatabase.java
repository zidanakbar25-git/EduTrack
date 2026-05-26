// database/AppDatabase.java
package com.example.edutrack.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.edutrack.model.MataKuliah;
import com.example.edutrack.model.Tugas;

@Database(entities = {MataKuliah.class, Tugas.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract MataKuliahDao mataKuliahDao();
    public abstract TugasDao tugasDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "edutrack_db"
            ).allowMainThreadQueries().build();
        }
        return instance;
    }
}