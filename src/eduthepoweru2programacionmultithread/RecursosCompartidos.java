import java.util.concurrent.ThreadLocalRandom;

/**
 * Recurso simple: una Impresora o un Escáner.
 */
class Recurso1 {
    private final String nombre;

    public Recurso1(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    // Simula el uso del recurso
    public void usar() {
        System.out.println(Thread.currentThread().getName() + " está usando " + nombre);
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(5, 50));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

/**
 * Clase principal que simula la gestión de dos recursos.
 * El método 'adquirirRecursos' implementa la prevención de Deadlock.
 */
public class RecursosCompartidos {

    /**
     * Adquiere el uso de dos recursos compartidos, garantizando un orden de bloqueo
     * basado en el código hash para evitar el deadlock.
     * * @param r1 El primer recurso solicitado.
     * @param r2 El segundo recurso solicitado.
     */
    public static void adquirirRecursos(Recurso r1, Recurso r2) {
        // Regla clave: Bloquear los recursos en un orden consistente.
        // Aquí usamos su hashCode como criterio de ordenación.
        Recurso primero = r1.hashCode() < r2.hashCode() ? r1 : r2;
        Recurso segundo = r1.hashCode() < r2.hashCode() ? r2 : r1;

        // Bloqueo del primer recurso
        synchronized (primero) {
            System.out.println(Thread.currentThread().getName() + " ha bloqueado: " + primero.getNombre());

            // Pausa simulada para aumentar la probabilidad de interbloqueo si no hubiera orden.
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Bloqueo del segundo recurso
            synchronized (segundo) {
                System.out.println(Thread.currentThread().getName() + " ha bloqueado: " + segundo.getNombre());

                // Usar ambos recursos
                r1.usar();
                r2.usar();

                System.out.println(Thread.currentThread().getName() + " liberando ambos recursos.");
            } // Liberación del segundo bloqueo
        }     // Liberación del primer bloqueo
    }

    public static void main(String[] args) throws InterruptedException {
        // 1. Definir los recursos compartidos
        Recurso impresora = new Recurso("Impresora Láser");
        Recurso escaner = new Recurso("Escáner de Red");

        // 2. Definir las tareas de los hilos
        // Hilo A: Intenta adquirir (Impresora, Escáner)
        Thread hiloA = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                adquirirRecursos(impresora, escaner);
            }
        }, "Hilo-Documento");

        // Hilo B: Intenta adquirir (Escáner, Impresora) - Orden inverso
        Thread hiloB = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                adquirirRecursos(escaner, impresora);
            }
        }, "Hilo-Digitalización");

        // 3. Ejecución
        System.out.println("Iniciando simulación de adquisición de recursos...");
        hiloA.start();
        hiloB.start();

        // 4. Esperar finalización
        hiloA.join();
        hiloB.join();

        System.out.println("\n✅ Simulación finalizada sin interbloqueos.");
    }
}
