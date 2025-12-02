package P1;



import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 🚗 Clase que representa un coche fabricado.
 * Cada coche tiene un ID único, modelo, empleado responsable y estadísticas de producción.
 */
class RealizarPedidos {
    private static final AtomicInteger generadorId = new AtomicInteger(0);

    private final int id;
    private final String nombre;
    private final long fecha;






    public RealizarPedidos(String nombre, String empleado) {
        this.id = generadorId.incrementAndGet(); // genera ID único seguro
        this.nombre = nombre;
        this.fecha = System.currentTimeMillis();





    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public long getFecha() { return fecha; }


    @Override
    public String toString() {
        return String.format("{id=%d, nombre='%s', fecha='%s'}",
                id,nombre, fecha);
    }
}

/**
 * 👷 Clase que representa un empleado (hilo) que fabrica coches.
 */
class Cliente implements Runnable {
    private final String nombre;
    private final String pedidoAsignado;
    private final int cantidadPedidos;
    private final Map<Integer, RealizarPedidos> registroGlobal;

    public Cliente(String nombre, String pedidoAsignado, int cantidadPedidos, Map<Integer, RealizarPedidos> registroGlobal) {
        this.nombre = nombre;
        this.pedidoAsignado = pedidoAsignado;
        this.cantidadPedidos = cantidadPedidos;
        this.registroGlobal = registroGlobal;
    }


    @Override
    public void run() {
        for (int i = 0; i < cantidadPedidos; i++) {
           RealizarPedidos pedido = new RealizarPedidos(pedidoAsignado, nombre);
            registroGlobal.put(pedido.getId(), pedido);
            System.out.println(pedidoAsignado + " Realizó pedido -> " + pedido);
        }

    }
}

/**
 * 🏭 Clase principal que simula una fábrica de coches con empleados multihilo.
 */
public class Pedidos {
    public static void main(String[] args) throws InterruptedException {
        // Registro global concurrente
        Map<Integer, RealizarPedidos> registroPedidos = new ConcurrentHashMap<>();

        // Crear empleados-hilos
        Thread emp1 = new Thread(new Cliente("Pedio1", "Luisa", 5, registroPedidos), "Empleado-1");
        Thread emp2 = new Thread(new Cliente("Pedido2",  "Pedro",5, registroPedidos), "Empleado-2");
        Thread emp3 = new Thread(new Cliente("Pedido3",  "Andres", 5, registroPedidos), "Empleado-3");
        Thread emp4 = new Thread(new Cliente("Pedido4",  "Pablo", 5, registroPedidos), "Empleado-4");

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
        int totalPedidos = registroPedidos.size();


         // Estadísticas por cliente
        Map<String, Long> produccionPorCliente = new ConcurrentHashMap<>();
        registroPedidos.values().forEach(c ->
                produccionPorCliente.merge(c.getNombre(), 1L, Long::sum)
        );



        // ======== RESULTADOS ========
        System.out.println("\n===== 📊 ESTADÍSTICAS FINALES =====");
        System.out.println("Total pedidos: " + totalPedidos);




        System.out.println("\nProducción por Cliente:");
       produccionPorCliente.forEach((nombre, cantidad) ->
                System.out.println(" - " + nombre + ": " + cantidad + " pedidos"));
    }
}
