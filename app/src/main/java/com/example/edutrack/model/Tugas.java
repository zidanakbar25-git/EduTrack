package com.example.edutrack.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "tugas",
        foreignKeys = @ForeignKey(
                entity = MataKuliah.class,
                parentColumns = "id",
                childColumns = "mataKuliahId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("mataKuliahId")}
)
public class Tugas {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String judul;
    public String deskripsi;   // catatan / opsional

    public int mataKuliahId;

    // "Tugas Biasa", "Tugas Besar", "Tugas Praktikum"
    public String jenisTugas;

    // epoch millis — di-set ke 23:59:59 WIB hari deadline
    public long deadline;

    // "pending" | "selesai" | "gagal"
    public String status;

    // timestamp saat ditandai selesai (nullable)
    public Long selesaiAt;

    public long createdAt;
}
