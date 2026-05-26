// model/Tugas.java
package com.example.edutrack.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "tugas",
        foreignKeys = @ForeignKey(
                entity = MataKuliah.class,
                parentColumns = "id",
                childColumns = "mataKuliahId",
                onDelete = ForeignKey.CASCADE
        )
)
public class Tugas {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String judul;
    public String deskripsi;
    public int mataKuliahId;
    // "Tugas Biasa", "Tugas Besar", "Tugas Praktikum"
    public String jenisTugas;
    public long deadline; // timestamp
    // "pending", "selesai", "gagal"
    public String status;
}