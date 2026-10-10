# EduTrack

Aplikasi manajemen tugas kuliah yang dirancang untuk membantu mahasiswa mengelola, melacak, dan menyelesaikan tugas akademik dengan lebih efisien.

## 📋 Daftar Isi

- [Fitur Utama](#fitur-utama)
- [Teknologi yang Digunakan](#teknologi-yang-digunakan)
- [Prasyarat Instalasi](#prasyarat-instalasi)
- [Instalasi](#instalasi)
- [Susunan Project](#susunan-project)
- [Penggunaan](#penggunaan)
- [Kontribusi](#kontribusi)
- [Lisensi](#lisensi)

## ✨ Fitur Utama

- **Manajemen Tugas**: Tambah, edit, dan hapus tugas kuliah dengan mudah
- **Penjadwalan**: Tetapkan deadline dan prioritas untuk setiap tugas
- **Pelacakan Status**: Pantau status tugas (belum dimulai, sedang dikerjakan, selesai)
- **Pengingat**: Dapatkan notifikasi untuk tugas yang akan datang
- **Kategori Mata Kuliah**: Organisir tugas berdasarkan mata kuliah
- **Riwayat Tugas**: Lihat riwayat tugas yang telah selesai
- **Antarmuka Intuitif**: Desain yang user-friendly dan mudah digunakan

## 🛠️ Teknologi yang Digunakan

- **Bahasa Pemrograman**: Java
- **Framework**: Gunakan framework Java standar atau Spring Boot (sesuaikan dengan project Anda)
- **Database**: SQL/Database lokal (sesuaikan dengan pilihan project Anda)
- **Build Tool**: Maven atau Gradle
- **IDE Rekomendasi**: IntelliJ IDEA, Eclipse, atau NetBeans

## 📦 Prasyarat Instalasi

Sebelum memulai, pastikan Anda telah menginstal:

- **Java Development Kit (JDK)**: Versi 11 atau lebih tinggi
- **Git**: Untuk cloning repository
- **Maven** (opsional): Jika menggunakan Maven sebagai build tool
- **Database**: Sesuai dengan konfigurasi project

Cek versi Java Anda:
```bash
java -version
javac -version
```

## 🚀 Instalasi

### 1. Clone Repository

```bash
git clone https://github.com/zidanakbar25-git/EduTrack.git
cd EduTrack
```

### 2. Setup Project

Jika menggunakan Maven:
```bash
mvn clean install
```

Jika menggunakan Gradle:
```bash
gradle build
```

### 3. Konfigurasi Database

- Buat database baru sesuai dengan nama di file konfigurasi
- Update connection string di file `application.properties` atau `config.xml`

### 4. Jalankan Aplikasi

```bash
mvn spring-boot:run
```

Atau compile dan jalankan:
```bash
javac -d bin src/**/*.java
java -cp bin MainApp
```

## 📁 Susunan Project

```
EduTrack/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/edutrack/
│   │   │       ├── controller/      # Kontroller aplikasi
│   │   │       ├── model/           # Model data
│   │   │       ├── service/         # Business logic
│   │   │       ├── repository/      # Akses database
│   │   │       └── Main.java        # Entry point
│   │   └── resources/
│   │       ├── application.properties
│   │       └── database/
│   └── test/
│       └── java/
│           └── com/edutrack/        # Unit tests
├── pom.xml                          # Maven configuration
├── .gitignore
├── README.md                        # Dokumentasi project
└── LICENSE                          # File lisensi MIT
```

## 💡 Penggunaan

### Contoh 1: Membuat Tugas Baru

```java
// Import yang diperlukan
import com.edutrack.model.Task;
import com.edutrack.service.TaskService;

// Membuat instance TaskService
TaskService taskService = new TaskService();

// Membuat tugas baru
Task newTask = new Task();
newTask.setTitle("Membuat Project Algoritma");
newTask.setSubject("Struktur Data");
newTask.setDeadline("2025-12-15");
newTask.setPriority("Tinggi");
newTask.setStatus("Belum Dimulai");

// Menyimpan tugas
taskService.saveTask(newTask);
System.out.println("Tugas berhasil ditambahkan!");
```

### Contoh 2: Menampilkan Semua Tugas

```java
// Mengambil semua tugas
List<Task> allTasks = taskService.getAllTasks();

// Menampilkan tugas
for (Task task : allTasks) {
    System.out.println("Judul: " + task.getTitle());
    System.out.println("Mata Kuliah: " + task.getSubject());
    System.out.println("Deadline: " + task.getDeadline());
    System.out.println("Status: " + task.getStatus());
    System.out.println("---");
}
```

### Contoh 3: Mengubah Status Tugas

```java
// Mengambil tugas berdasarkan ID
Task task = taskService.getTaskById(1);

// Mengubah status
task.setStatus("Sedang Dikerjakan");
taskService.updateTask(task);

System.out.println("Status tugas telah diperbarui!");
```

### Contoh 4: Menghapus Tugas

```java
// Menghapus tugas berdasarkan ID
taskService.deleteTask(1);
System.out.println("Tugas berhasil dihapus!");
```

## 🤝 Kontribusi

Kami menyambut kontribusi dari siapa saja! Untuk berkontribusi pada project ini:

1. **Fork** repository ini
2. **Clone** fork Anda ke komputer lokal:
   ```bash
   git clone https://github.com/username/EduTrack.git
   ```
3. **Buat branch** baru untuk fitur atau perbaikan Anda:
   ```bash
   git checkout -b feature/fitur-baru
   ```
4. **Commit** perubahan Anda dengan pesan yang jelas:
   ```bash
   git commit -m "Tambah fitur baru: deskripsi fitur"
   ```
5. **Push** ke branch Anda:
   ```bash
   git push origin feature/fitur-baru
   ```
6. **Buat Pull Request** dengan deskripsi lengkap tentang perubahan Anda

### Pedoman Kontribusi

- Ikuti coding style yang sudah ada
- Pastikan code Anda ter-test dengan baik
- Update dokumentasi jika diperlukan
- Pastikan tidak ada konflik dengan branch utama

## 📄 Lisensi

Project ini dilisensikan di bawah [MIT License](LICENSE).

Anda bebas menggunakan, memodifikasi, dan mendistribusikan project ini, selama Anda menyertakan pemberitahuan copyright dan lisensi.

Untuk informasi lebih lanjut, lihat file [LICENSE](LICENSE).

---

**Dikembangkan dengan ❤️ oleh [zidanakbar25-git](https://github.com/zidanakbar25-git)**

Jika Anda memiliki pertanyaan atau saran, silakan buka [issue](https://github.com/zidanakbar25-git/EduTrack/issues) di repository ini.
