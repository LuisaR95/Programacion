package PracticaExamen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 🚗 Clase que representa un coche fabricado.
 */
class Coche8 {
    private static final AtomicInteger generadorId = new AtomicInteger(0);
    private final int id;
    private final String modelo;
    private final String empleado;
    private final int piezasUsadas;
    private final long tiempoProduccion; // milisegundos
    private final int calidad; // 0–10, valor aleatorio

    public Coche8(String modelo, String empleado) {
        this.id = generadorId.incrementAndGet();
        this.modelo = modelo;
        this.empleado = empleado;

        ThreadLocalRandom r = ThreadLocalRandom.current();
        this.piezasUsadas = r.nextInt(50, 151);
        this.tiempoProduccion = r.nextLong(500, 2000);
        this.calidad = r.nextInt(0, 11);

        try {
            Thread.sleep(tiempoProduccion);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getId() { return id; }
    public String getModelo() { return modelo; }
    public String getEmpleado() { return empleado; }
    public int getPiezasUsadas() { return piezasUsadas; }
    public long getTiempoProduccion() { return tiempoProduccion; }
    public int getCalidad() { return calidad; }

    @Override
    public String toString() {
        return String.format("Coche8{id=%d, modelo='%s', empleado='%s', piezas=%d, tiempo=%dms, calidad=%d}",
                id, modelo, empleado, piezasUsadas, tiempoProduccion, calidad);
    }
}

/**
 * 👷 Empleado (hilo) que fabrica varios coches y los guarda en una lista.
 */
class Empleado8 implements Runnable {
    private final String nombre;
    private final String modeloAsignado;
    private final int cantidadCoches;
    private final List<Coche8> listaLocal;
    private final List<Coche8> registroGlobal;

    public Empleado8(String nombre, String modeloAsignado, int cantidadCoches, List<Coche8> registroGlobal) {
        this.nombre = nombre;
        this.modeloAsignado = modeloAsignado;
        this.cantidadCoches = cantidadCoches;
        this.listaLocal = new ArrayList<>();
        this.registroGlobal = registroGlobal;
    }

    @Override
    public void run() {
        for (int i = 0; i < cantidadCoches; i++) {
            Coche8 coche = new Coche8(modeloAsignado, nombre);
            listaLocal.add(coche);
            synchronized (registroGlobal) {
                registroGlobal.add(coche); // sincronizado para evitar conflicto
            }
            System.out.println("👷 " + nombre + " fabricó -> " + coche);
        }

        // Estadísticas por empleado
        double promCalidad = listaLocal.stream().mapToInt(Coche8::getCalidad).average().orElse(0);
        System.out.printf("✅ %s terminó (%d coches). Calidad promedio: %.2f%n", nombre, cantidadCoches, promCalidad);
    }
}

/**
 * 🏭 Clase principal: Fábrica de coches (versión 8)
 */
public class FabricaCoches8 {
    public static void main(String[] args) throws InterruptedException {
        List<Coche8> registroCoches = Collections.synchronizedList(new ArrayList<>());

        // Crear hilos-empleados
        Thread emp1 = new Thread(new Empleado8("Carlos", "Sedán", 5, registroCoches));
        Thread emp2 = new Thread(new Empleado8("Lucía", "SUV", 5, registroCoches));
        Thread emp3 = new Thread(new Empleado8("Miguel", "Deportivo", 5, registroCoches));
        Thread emp4 = new Thread(new Empleado8("Ana", "Camioneta", 5, registroCoches));

        // Iniciar
        emp1.start();
        emp2.start();
        emp3.start();
        emp4.start();

        // Esperar
        emp1.join();
        emp2.join();
        emp3.join();
        emp4.join();

        // === Estadísticas globales ===
        int totalCoches = registroCoches.size();
        int totalPiezas = registroCoches.stream().mapToInt(Coche8::getPiezasUsadas).sum();
        double promTiempo = registroCoches.stream().mapToLong(Coche8::getTiempoProduccion).average().orElse(0);
        double promCalidad = registroCoches.stream().mapToInt(Coche8::getCalidad).average().orElse(0);

        // Agrupar por modelo
        Map<String, Long> porModelo = registroCoches.stream()
                .collect(Collectors.groupingBy(Coche8::getModelo, Collectors.counting()));

        // Agrupar por empleado
        Map<String, Long> porEmpleado = registroCoches.stream()
                .collect(Collectors.groupingBy(Coche8::getEmpleado, Collectors.counting()));

        System.out.println("\n===== 📊 ESTADÍSTICAS FINALES =====");
        System.out.println("Total coches: " + totalCoches);
        System.out.println("Total piezas: " + totalPiezas);
        System.out.printf("⏱ Tiempo promedio: %.2f ms%n", promTiempo);
        System.out.printf("⭐ Calidad promedio global: %.2f%n", promCalidad);

        System.out.println("\nProducción por modelo:");
        porModelo.forEach((m, n) -> System.out.println(" - " + m + ": " + n));

        System.out.println("\nProducción por empleado:");
        porEmpleado.forEach((e, n) -> System.out.println(" - " + e + ": " + n));
    }
}

