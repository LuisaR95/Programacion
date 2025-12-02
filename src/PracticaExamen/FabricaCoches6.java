package PracticaExamen;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 🚗 Clase que representa un coche fabricado.
 * Cada coche tiene un ID único, modelo, empleado responsable y estadísticas de producción.
 */
class Coche6 {
    private static final AtomicInteger generadorId = new AtomicInteger(0);

    private final int id;
    private final String modelo;
    private final String empleado;
    private final int piezasUsadas;
    private final long tiempoProduccion; // en milisegundos

    public Coche6(String modelo, String empleado) {
        this.id = generadorId.incrementAndGet(); // genera ID único seguro
        this.modelo = modelo;
        this.empleado = empleado;

        // Simulación de características aleatorias
        this.piezasUsadas = ThreadLocalRandom.current().nextInt(50, 151);
        this.tiempoProduccion = ThreadLocalRandom.current().nextLong(500, 2000);

        // Simula tiempo real de producción
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

    @Override
    public String toString() {
        return String.format("Coche6{id=%d, modelo='%s', empleado='%s', piezas=%d, tiempo=%dms}",
                id, modelo, empleado, piezasUsadas, tiempoProduccion);
    }
}

/**
 * 👷 Clase que representa un empleado (hilo) que fabrica coches.
 */
class Empleado6 implements Runnable {
    private final String nombre;
    private final String modeloAsignado;
    private final int cantidadCoches;
    private final Map<Integer, Coche6> registroGlobal;

    public Empleado6(String nombre, String modeloAsignado, int cantidadCoches, Map<Integer, Coche6> registroGlobal) {
        this.nombre = nombre;
        this.modeloAsignado = modeloAsignado;
        this.cantidadCoches = cantidadCoches;
        this.registroGlobal = registroGlobal;
    }

    @Override
    public void run() {
        for (int i = 0; i < cantidadCoches; i++) {
            Coche6 coche = new Coche6(modeloAsignado, nombre);
            registroGlobal.put(coche.getId(), coche);
            System.out.println("👷 " + nombre + " fabricó -> " + coche);
        }
        System.out.println("✅ " + nombre + " terminó de fabricar sus coches (" + cantidadCoches + ")");
    }
}

/**
 * 🏭 Clase principal que simula una fábrica de coches con empleados multihilo.
 */
public class FabricaCoches6 {
    public static void main(String[] args) throws InterruptedException {
        // Registro global concurrente
        Map<Integer, Coche6> registroCoches = new ConcurrentHashMap<>();

        // Crear empleados-hilos
        Thread emp1 = new Thread(new Empleado6("Carlos", "Sedán", 5, registroCoches), "Empleado-1");
        Thread emp2 = new Thread(new Empleado6("Lucía", "SUV", 5, registroCoches), "Empleado-2");
        Thread emp3 = new Thread(new Empleado6("Miguel", "Deportivo", 5, registroCoches), "Empleado-3");
        Thread emp4 = new Thread(new Empleado6("Ana", "Camioneta", 5, registroCoches), "Empleado-4");

        // Iniciar producción
        emp1.start();
        emp2.start();
        emp3.start();
        emp4.start();

        // Esperar finalización
        emp1.join();
        emp2.join();
        emp3.join();
        emp4.join();

        // ======== ESTADÍSTICAS GLOBALES ========
        int totalCoches = registroCoches.size();
        int totalPiezas = registroCoches.values().stream().mapToInt(Coche6::getPiezasUsadas).sum();
        double tiempoPromedio = registroCoches.values().stream()
                .mapToLong(Coche6::getTiempoProduccion)
                .average()
                .orElse(0);

        // Estadísticas por modelo
        Map<String, Long> produccionPorModelo = new ConcurrentHashMap<>();
        registroCoches.values().forEach(c ->
                produccionPorModelo.merge(c.getModelo(), 1L, Long::sum)
        );

        // Estadísticas por empleado
        Map<String, Long> produccionPorEmpleado = new ConcurrentHashMap<>();
        registroCoches.values().forEach(c ->
                produccionPorEmpleado.merge(c.getEmpleado(), 1L, Long::sum)
        );

        // ======== RESULTADOS ========
        System.out.println("\n===== 📊 ESTADÍSTICAS FINALES =====");
        System.out.println("Total coches producidos: " + totalCoches);
        System.out.println("Total piezas utilizadas: " + totalPiezas);
        System.out.printf("Tiempo promedio de producción: %.2f ms%n", tiempoPromedio);

        System.out.println("\nProducción por modelo:");
        produccionPorModelo.forEach((modelo, cantidad) ->
                System.out.println(" - " + modelo + ": " + cantidad + " unidades"));

        System.out.println("\nProducción por empleado:");
        produccionPorEmpleado.forEach((empleado, cantidad) ->
                System.out.println(" - " + empleado + ": " + cantidad + " coches fabricados"));
    }
}
