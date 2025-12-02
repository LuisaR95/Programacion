package PracticaExamen;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 🚗 Clase que representa un tramo de carretera (recurso compartido)
 */
class Tramo {
    private final String nombre;

    public Tramo(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}

/**
 * 🚙 Clase que representa un coche que intenta pasar por dos tramos.
 * Usa hashCode() para evitar interbloqueos (deadlocks).
 */
class Coche9 implements Runnable {
    private final String nombre;
    private final Tramo tramoA;
    private final Tramo tramoB;

    public Coche9(String nombre, Tramo tramoA, Tramo tramoB) {
        this.nombre = nombre;
        this.tramoA = tramoA;
        this.tramoB = tramoB;
    }

    @Override
    public void run() {
        // Ordenar los bloqueos de los tramos según hashCode()
        Tramo primero = tramoA.hashCode() < tramoB.hashCode() ? tramoA : tramoB;
        Tramo segundo = tramoA.hashCode() < tramoB.hashCode() ? tramoB : tramoA;

        // Solo intenta cruzar una vez
        synchronized (primero) {
            System.out.println("🚗 " + nombre + " bloqueó el tramo " + primero.getNombre());
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(200, 500));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            synchronized (segundo) {
                System.out.println("✅ " + nombre + " cruzó los tramos " +
                        primero.getNombre() + " y " + segundo.getNombre());
            }
        }

        System.out.println("🏁 " + nombre + " ha terminado su recorrido.\n");
    }
}

/**
 * 🏁 Clase principal que lanza varios coches sobre tramos compartidos.
 */
public class FabricaCoches9 {
    public static void main(String[] args) throws InterruptedException {
        // Crear tramos de carretera
        Tramo tramoNorte = new Tramo("Norte");
        Tramo tramoSur = new Tramo("Sur");
        Tramo tramoEste = new Tramo("Este");
        Tramo tramoOeste = new Tramo("Oeste");

        // Crear coches con pares de tramos
        Thread c1 = new Thread(new Coche9("Coche-1", tramoNorte, tramoEste));
        Thread c2 = new Thread(new Coche9("Coche-2", tramoEste, tramoSur));
        Thread c3 = new Thread(new Coche9("Coche-3", tramoSur, tramoOeste));
        Thread c4 = new Thread(new Coche9("Coche-4", tramoOeste, tramoNorte));

        // Iniciar los coches
        c1.start();
        c2.start();
        c3.start();
        c4.start();

        // Esperar a que todos terminen
        c1.join();
        c2.join();
        c3.join();
        c4.join();

        System.out.println("✅ Todos los coches han cruzado sin deadlock 🚦");
    }
}
