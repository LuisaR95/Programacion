package PracticaExamen;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 🚗 Clase que representa un coche fabricado.
 * Cada coche tiene un ID único, modelo, empleado responsable y estadísticas de producción.
 */
class Movil {
    private static final AtomicInteger generadorId = new AtomicInteger(0);

    private final int id;
    private final String modelo;
    private final String empleado;
    private final int piezasUsadas;
    private final long tiempoProduccion; // en milisegundos

    public Movil(String modelo, String empleado) {
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
        return String.format("{id=%d, modelo='%s', empleado='%s', piezas=%d, tiempo=%dms}",
                id, modelo, empleado, piezasUsadas, tiempoProduccion);
    }
}

/**
 * 👷 Clase que representa un empleado (hilo) que fabrica coches.
 */
class Empleado implements Runnable {
    private final String nombre;
    private final String modeloAsignado;
    private final int cantidadMoviles;
    private final Map<Integer, Movil> registroGlobal;

    public Empleado(String nombre, String modeloAsignado, int cantidadMoviles, Map<Integer, Movil> registroGlobal) {
        this.nombre = nombre;
        this.modeloAsignado = modeloAsignado;
        this.cantidadMoviles = cantidadMoviles;
        this.registroGlobal = registroGlobal;
    }

    @Override
    public void run() {
        for (int i = 0; i < cantidadMoviles; i++) {
            Movil movil = new Movil(modeloAsignado, nombre);
            registroGlobal.put(movil.getId(), movil);
            System.out.println("👷 " + nombre + " fabricó -> " + movil);
        }
        System.out.println("✅ " + nombre + " terminó de fabricar sus coches (" + cantidadMoviles + ")");
    }
}

/**
 * 🏭 Clase principal que simula una fábrica de coches con empleados multihilo.
 */
public class FabricaMoviles {
    public static void main(String[] args) throws InterruptedException {
        // Registro global concurrente
        Map<Integer, Movil> registroMoviles = new ConcurrentHashMap<>();

        // Crear empleados-hilos
        Thread emp1 = new Thread(new Empleado("Carlos", "Sedán", 5, registroMoviles), "Empleado-1");
        Thread emp2 = new Thread(new Empleado("Lucía", "SUV", 5, registroMoviles), "Empleado-2");
        Thread emp3 = new Thread(new Empleado("Miguel", "Deportivo", 5, registroMoviles), "Empleado-3");
        Thread emp4 = new Thread(new Empleado("Ana", "Camioneta", 5, registroMoviles), "Empleado-4");

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
        int totalMoviles = registroMoviles.size();
        int totalPiezas = registroMoviles.values().stream().mapToInt(Movil::getPiezasUsadas).sum();
        double tiempoPromedio = registroMoviles.values().stream()
                .mapToLong(Movil::getTiempoProduccion)
                .average()
                .orElse(0);

        // Estadísticas por modelo
        Map<String, Long> produccionPorModelo = new ConcurrentHashMap<>();
        registroMoviles.values().forEach(c ->
                produccionPorModelo.merge(c.getModelo(), 1L, Long::sum)
        );

        // Estadísticas por empleado
        Map<String, Long> produccionPorEmpleado = new ConcurrentHashMap<>();
        registroMoviles.values().forEach(c ->
                produccionPorEmpleado.merge(c.getEmpleado(), 1L, Long::sum)
        );

        // ======== RESULTADOS ========
        System.out.println("\n===== 📊 ESTADÍSTICAS FINALES =====");
        System.out.println("Total coches producidos: " + totalMoviles);
        System.out.println("Total piezas utilizadas: " + totalPiezas);
        System.out.printf("Tiempo promedio de producción: %.2f ms%n", tiempoPromedio);

        System.out.println("\nProducción por modelo:");
        produccionPorModelo.forEach((modelo, cantidad) ->
                System.out.println(" - " + modelo + ": " + cantidad + " unidades"));

        System.out.println("\nProducción por empleado:");
        produccionPorEmpleado.forEach((empleado, cantidad) ->
                System.out.println(" - " + empleado + ": " + cantidad + " moviles fabricados"));
    }
}
