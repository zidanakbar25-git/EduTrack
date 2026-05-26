// model/MataKuliah.java
package com.example.edutrack.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mata_kuliah")
public class MataKuliah {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String nama;
}