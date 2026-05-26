package com.example.edutrack;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.edutrack.ui.dashboard.DashboardFragment;
import com.example.edutrack.ui.matakuliah.MataKuliahFragment;
import com.example.edutrack.ui.riwayat.RiwayatFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load fragment pertama
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new DashboardFragment())
                .commit();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_dashboard) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new DashboardFragment()).commit();
            } else if (id == R.id.nav_matakuliah) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new MataKuliahFragment()).commit();
            } else if (id == R.id.nav_riwayat) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new RiwayatFragment()).commit();
            }
            return true;
        });
    }
}