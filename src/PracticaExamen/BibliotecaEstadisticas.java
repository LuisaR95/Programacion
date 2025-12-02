package PracticaExamen;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

class Libro1 {
    private final String titulo;
    public Libro1(String titulo) { this.titulo = titulo; }
    public String getTitulo() { return titulo; }
}

class Estudiante1 implements Runnable {
    private final String nombre;
    private final Libro1 libro1;
    private final Libro1 libro2;

    // Contadores globales (thread-safe)
    private static final Map<String, AtomicInteger> libroContador = new ConcurrentHashMap<>();
    private static final Map<String, AtomicInteger> estudianteContador = new ConcurrentHashMap<>();

    public Estudiante1(String nombre, Libro1 libro1, Libro1 libro2) {
        this.nombre = nombre;
        this.libro1 = libro1;
        this.libro2 = libro2;

        libroContador.putIfAbsent(libro1.getTitulo(), new AtomicInteger(0));
        libroContador.putIfAbsent(libro2.getTitulo(), new AtomicInteger(0));
        estudianteContador.putIfAbsent(nombre, new AtomicInteger(0));
    }

    @Override
    public void run() {
        Libro1 primero = libro1.hashCode() < libro2.hashCode() ? libro1 : libro2;
        Libro1 segundo = libro1.hashCode() < libro2.hashCode() ? libro2 : libro1;

        synchronized (primero) {
            System.out.println("📖 " + nombre + " tomó " + primero.getTitulo());
            libroContador.get(primero.getTitulo()).incrementAndGet();
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            synchronized (segundo) {
                System.out.println("✅ " + nombre + " está leyendo ambos libros.");
                libroContador.get(segundo.getTitulo()).incrementAndGet();
                estudianteContador.get(nombre).incrementAndGet();
            }
        }
    }

    // Métodos para acceder a estadísticas
    public static Map<String, AtomicInteger> getLibroContador() { return libroContador; }
    public static Map<String, AtomicInteger> getEstudianteContador() { return estudianteContador; }
}

public class BibliotecaEstadisticas {
    public static void main(String[] args) throws InterruptedException {
        Libro1 l1 = new Libro1("Matemáticas");
        Libro1 l2 = new Libro1("Física");

        Thread e1 = new Thread(new Estudiante1("Ana", l1, l2));
        Thread e2 = new Thread(new Estudiante1("Luis", l2, l1));

        e1.start();
        e2.start();

        // Esperar a que todos los estudiantes terminen
        e1.join();
        e2.join();

        // ===== ESTADÍSTICAS FINALES =====
        System.out.println("\n===== 📊 ESTADÍSTICAS FINALES =====");
        System.out.println("Cantidad de veces que se tomó cada libro:");
        Estudiante1.getLibroContador().forEach((titulo, contador) ->
                System.out.println(" - " + titulo + ": " + contador.get() + " veces")
        );

        System.out.println("\nCantidad de veces que cada estudiante leyó ambos libros:");
        Estudiante1.getEstudianteContador().forEach((nombre, contador) ->
                System.out.println(" - " + nombre + ": " + contador.get() + " veces")
        );
    }
}

