package PracticaExamen;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 📖 Clase que representa un préstamo de libro.
 */
class PrestamoLibro {
    private static final AtomicInteger generadorId = new AtomicInteger(0);

    private final int id;
    private final String libro;
    private final String lector;
    private final String bibliotecario;
    private final long duracionPrestamo; // en ms

    public PrestamoLibro(String libro, String lector, String bibliotecario) {
        this.id = generadorId.incrementAndGet();
        this.libro = libro;
        this.lector = lector;
        this.bibliotecario = bibliotecario;
        this.duracionPrestamo = ThreadLocalRandom.current().nextLong(1000, 4000);

        // Simula tiempo de gestión
        try {
            Thread.sleep(duracionPrestamo / 10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getId() { return id; }
    public String getLibro() { return libro; }
    public String getLector() { return lector; }
    public String getBibliotecario() { return bibliotecario; }
    public long getDuracionPrestamo() { return duracionPrestamo; }

    @Override
    public String toString() {
        return String.format("Prestamo{id=%d, libro='%s', lector='%s', bibliotecario='%s', duracion=%dms}",
                id, libro, lector, bibliotecario, duracionPrestamo);
    }
}

/**
 * 🧑‍🏫 Clase que representa un bibliotecario (hilo) que gestiona préstamos.
 */
class Bibliotecario implements Runnable {
    private final String nombre;
    private final int prestamosAtendidos;
    private final Map<Integer, PrestamoLibro> registroPrestamos;

    public Bibliotecario(String nombre, int prestamosAtendidos, Map<Integer, PrestamoLibro> registroPrestamos) {
        this.nombre = nombre;
        this.prestamosAtendidos = prestamosAtendidos;
        this.registroPrestamos = registroPrestamos;
    }

    @Override
    public void run() {
        for (int i = 0; i < prestamosAtendidos; i++) {
            String libro = "Libro-" + ThreadLocalRandom.current().nextInt(1, 8);
            String lector = "Lector-" + ThreadLocalRandom.current().nextInt(1, 6);
            PrestamoLibro prestamo = new PrestamoLibro(libro, lector, nombre);
            registroPrestamos.put(prestamo.getId(), prestamo);
            System.out.println("📚 " + nombre + " gestionó -> " + prestamo);
        }
        System.out.println("✅ " + nombre + " terminó de atender sus préstamos");
    }
}

/**
 * 📊 Clase principal: Biblioteca Multihilo con más estadísticas
 */
public class BibliotecaMultihiloConEstadisticas {
    public static void main(String[] args) throws InterruptedException {
        Map<Integer, PrestamoLibro> registroPrestamos = new ConcurrentHashMap<>();

        // Crear bibliotecarios (hilos)
        Thread b1 = new Thread(new Bibliotecario("Sofía", 6, registroPrestamos));
        Thread b2 = new Thread(new Bibliotecario("Andrés", 6, registroPrestamos));
        Thread b3 = new Thread(new Bibliotecario("Valeria", 6, registroPrestamos));

        b1.start(); b2.start(); b3.start();
        b1.join(); b2.join(); b3.join();

        // ======= ESTADÍSTICAS =======
        long totalDuracion = registroPrestamos.values().stream()
                .mapToLong(PrestamoLibro::getDuracionPrestamo).sum();

        double duracionPromedio = registroPrestamos.values().stream()
                .mapToLong(PrestamoLibro::getDuracionPrestamo).average().orElse(0);

        // --- Estadísticas por libro ---
        Map<String, Long> prestamosPorLibro = new ConcurrentHashMap<>();
        registroPrestamos.values().forEach(p ->
                prestamosPorLibro.merge(p.getLibro(), 1L, Long::sum)
        );

        // --- Estadísticas por lector ---
        Map<String, Long> prestamosPorLector = new ConcurrentHashMap<>();
        registroPrestamos.values().forEach(p ->
                prestamosPorLector.merge(p.getLector(), 1L, Long::sum)
        );

        // --- Estadísticas por bibliotecario ---
        Map<String, Long> prestamosPorBibliotecario = new ConcurrentHashMap<>();
        registroPrestamos.values().forEach(p ->
                prestamosPorBibliotecario.merge(p.getBibliotecario(), 1L, Long::sum)
        );

        // --- Libro con préstamo más largo y más corto ---
        PrestamoLibro maxDuracion = registroPrestamos.values().stream()
                .max(Comparator.comparingLong(PrestamoLibro::getDuracionPrestamo)).orElse(null);
        PrestamoLibro minDuracion = registroPrestamos.values().stream()
                .min(Comparator.comparingLong(PrestamoLibro::getDuracionPrestamo)).orElse(null);

        // --- Lector con más tiempo total acumulado ---
        Map<String, Long> tiempoPorLector = new HashMap<>();
        for (PrestamoLibro p : registroPrestamos.values()) {
            tiempoPorLector.merge(p.getLector(), p.getDuracionPrestamo(), Long::sum);
        }

        Map.Entry<String, Long> lectorTop = tiempoPorLector.entrySet().stream()
                .max(Map.Entry.comparingByValue()).orElse(null);

        // ======= RESULTADOS =======
        System.out.println("\n===== 📊 ESTADÍSTICAS DE LA BIBLIOTECA =====");
        System.out.println("Total préstamos gestionados: " + registroPrestamos.size());
        System.out.println("Duración total de préstamos: " + totalDuracion + " ms");
        System.out.printf("Duración promedio de préstamo: %.2f ms%n", duracionPromedio);

        System.out.println("\n📚 Préstamos por libro:");
        prestamosPorLibro.forEach((libro, cantidad) ->
                System.out.println(" - " + libro + ": " + cantidad + " veces"));

        System.out.println("\n👤 Préstamos por lector:");
        prestamosPorLector.forEach((lector, cantidad) ->
                System.out.println(" - " + lector + ": " + cantidad + " préstamos"));

        System.out.println("\n🧑‍💼 Préstamos por bibliotecario:");
        prestamosPorBibliotecario.forEach((biblio, cantidad) ->
                System.out.println(" - " + biblio + ": " + cantidad + " gestionados"));

        if (maxDuracion != null && minDuracion != null) {
            System.out.println("\n⏱️ Préstamo más largo: " + maxDuracion);
            System.out.println("⏱️ Préstamo más corto: " + minDuracion);
        }

        if (lectorTop != null) {
            System.out.println("\n🏆 Lector con más tiempo acumulado: " +
                    lectorTop.getKey() + " (" + lectorTop.getValue() + " ms totales)");
        }

        System.out.println("\n✅ Fin de la simulación bibliotecaria multihilo.");
    }
}
