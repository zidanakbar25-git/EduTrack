package com.example.edutrack.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateHelper {

    private static final TimeZone WIB = TimeZone.getTimeZone("Asia/Jakarta");
    private static final Locale ID  = new Locale("id", "ID");

    /** Format tanggal: 1 Agt 2026 */
    public static String formatTanggal(long epochMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", ID);
        sdf.setTimeZone(WIB);
        return sdf.format(new Date(epochMillis));
    }

    /**
     * Konversi tanggal yang dipilih DatePicker menjadi epoch milis
     * di-set ke 23:59:59.999 WIB hari itu.
     */
    public static long toDeadlineMillis(int year, int month, int day) {
        Calendar cal = Calendar.getInstance(WIB);
        cal.set(year, month, day, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis();
    }

    /** Tengah malam WIB hari ini (00:00:00.000) */
    public static long todayStartMillis() {
        Calendar cal = Calendar.getInstance(WIB);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /** Apakah deadline sudah lewat dari sekarang? */
    public static boolean isTerlambat(long deadlineMillis) {
        return deadlineMillis < System.currentTimeMillis();
    }

    /** Apakah deadline hari ini (tanggal sama, WIB)? */
    public static boolean isHariIni(long deadlineMillis) {
        Calendar dlCal = Calendar.getInstance(WIB);
        dlCal.setTimeInMillis(deadlineMillis);

        Calendar nowCal = Calendar.getInstance(WIB);

        return dlCal.get(Calendar.YEAR) == nowCal.get(Calendar.YEAR)
                && dlCal.get(Calendar.DAY_OF_YEAR) == nowCal.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Hitung sisa hari (floor).
     * Negatif = terlambat, 0 = hari ini, positif = N hari lagi.
     */
    public static int sisaHari(long deadlineMillis) {
        long todayStart = todayStartMillis();
        // Ambil awal hari deadline WIB
        Calendar dlCal = Calendar.getInstance(WIB);
        dlCal.setTimeInMillis(deadlineMillis);
        dlCal.set(Calendar.HOUR_OF_DAY, 0);
        dlCal.set(Calendar.MINUTE, 0);
        dlCal.set(Calendar.SECOND, 0);
        dlCal.set(Calendar.MILLISECOND, 0);
        long diffMs = dlCal.getTimeInMillis() - todayStart;
        return (int) TimeUnit.MILLISECONDS.toDays(diffMs);
    }

    /**
     * Label sisa hari untuk card tugas.
     * @return "Terlambat!" / "Hari ini!" / "N hari lagi"
     */
    public static String labelSisaHari(long deadlineMillis) {
        int sisa = sisaHari(deadlineMillis);
        if (sisa < 0) return "Terlambat!";
        if (sisa == 0) return "Hari ini!";
        return sisa + " hari lagi";
    }

    /**
     * Warna hex untuk label sisa hari.
     * Merah / Oranye / Kuning / Hijau
     */
    public static String warnaSisaHari(long deadlineMillis) {
        int sisa = sisaHari(deadlineMillis);
        if (sisa < 0)  return "#EF4444"; // Merah
        if (sisa == 0) return "#F97316"; // Oranye
        if (sisa <= 3) return "#EAB308"; // Kuning
        return "#22C55E";                // Hijau
    }

    /** Apakah tanggal yang dipilih (year,month,day) adalah hari ini atau setelahnya? */
    public static boolean isDeadlineValid(int year, int month, int day) {
        Calendar picked = Calendar.getInstance(WIB);
        picked.set(year, month, day, 0, 0, 0);
        picked.set(Calendar.MILLISECOND, 0);

        Calendar todayStart = Calendar.getInstance(WIB);
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);

        return !picked.before(todayStart);
    }
}
