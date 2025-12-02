package Examen;



import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 🚗 Clase que representa un coche fabricado.
 * Cada coche tiene un ID único, modelo, empleado responsable y estadísticas de producción.
 */
class RealizarPedidos {
    private static final AtomicInteger generadorId = new AtomicInteger(0);

    private final int id;
    private  String nombre;
    private final long fecha;
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");






    public RealizarPedidos(String cliente) {
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
                id,nombre, sdf.format(fecha));
    }
}

/**
 * 👷 Clase que representa un empleado (hilo) que fabrica coches.
 */

/**
 * 🏭 Clase principal que simula una fábrica de coches con empleados multihilo.
 */
public class Pedidos {
    public static void main(String[] args) throws InterruptedException {

        final int MAX_PEDIDOS = 10;
        final int NUM_PEDIDOS = 10;
        List<RealizarPedidos> pedidos = Collections.synchronizedList(new ArrayList<>());
        Map<String, AtomicInteger> pedidosPorCliente = new ConcurrentHashMap<>();
        List<Thread> threads = new ArrayList<>();
        Random random = new Random();
        //1. declaracion y ejecucion de los threads
        for (int i = 0; i < MAX_PEDIDOS; i++) {
            Thread hilo = new Thread(() -> {
                for (int j = 0; j < NUM_PEDIDOS; j++) {
                    //Crear cliente aleatorio
                    String cliente = "Cliente - " + random.nextInt(10);

                    RealizarPedidos pedido = new RealizarPedidos(cliente);

                    pedidos.add(pedido);
                   // System.out.println(pedido);
                    pedidosPorCliente.computeIfAbsent(cliente, k -> new AtomicInteger()).incrementAndGet();
                }
            });


            // Iniciar hilo y almacenamiento en lista de threads
            hilo.start();
            threads.add(hilo);

        }
        System.out.println("Todos lo threads estan en ejecución");
// 2. Esperando a completar que todos los hilos acaben
        for (Thread thread : threads) {
           try {
               thread.join();
           }catch (InterruptedException e){
               throw new RuntimeException(e);
           }

        }
        System.out.println("Todos los theads han terminado.");
        //3. mOstrar estadisticas
        System.out.println("*** Pedidos realizados por clientes: ");
        for (RealizarPedidos pedido : pedidos) {
            System.out.println(pedido);
        }
        System.out.println("*** Numero total de pedidos: " + pedidos.size());
        System.out.println("*** Cantidad de pedidos por cliente: ");
        int cantidad = 0;

        for (Map.Entry<String, AtomicInteger> entry : pedidosPorCliente.entrySet()) {
            String cliente = entry.getKey();
            int pedidosCliente = entry.getValue().get();
            System.out.println("El cliente " + cliente + " ha realizado " + pedidosCliente + " pedidos.");
            cantidad += pedidosCliente;
        }
        System.out.println("📊 Total de pedidos realizados: " + cantidad);
    }

}
