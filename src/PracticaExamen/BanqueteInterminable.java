import java.util.concurrent.ThreadLocalRandom;

/**
 * 🔪 Clase que representa un Recurso (Cuchillo Chef o Tabla de Cortar).
 * Se corresponde con la clase 'Recurso' del enunciado.
 */
class Recurso {
    private final String nombre;

    public Recurso(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    // Simula la acción de usar el recurso.
    public void usar() {
        System.out.println("... " + Thread.currentThread().getName() + " está utilizando el recurso: " + nombre);
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(5, 50));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

/**
 * 👨‍🍳 Clase que representa a un Chef (el hilo) que intenta adquirir los recursos.
 */
class Chef implements Runnable {
    private final String nombre;
    private final Recurso primeroSolicitado; // Recurso 1 (Ej: Tabla)
    private final Recurso segundoSolicitado; // Recurso 2 (Ej: Cuchillo)

    public Chef(String nombre, Recurso recurso1, Recurso recurso2) {
        this.nombre = nombre;
        this.primeroSolicitado = recurso1;
        this.segundoSolicitado = recurso2;
    }

    @Override
    public void run() {
        System.out.println("Chef " + nombre + " iniciando. Necesita: " +
                primeroSolicitado.getNombre() + " y " + segundoSolicitado.getNombre());

        // La lógica de bloqueo se delega al método estático para garantizar el orden global.
        BanqueteInterminable.adquirirRecursos(primeroSolicitado, segundoSolicitado);

        System.out.println("✅ Chef " + nombre + " terminó su tarea y liberó los recursos.");
    }
}

/**
 * 🍽️ Clase principal que simula el entorno de la cocina y el Deadlock.
 * Implementa la prevención de Deadlock con orden de bloqueo.
 */
public class BanqueteInterminable {

    /**
     * **MÉTODO SOLUCIÓN DEL DEADLOCK (Problema 2)**
     * * Adquiere el uso de dos recursos compartidos, garantizando un orden de bloqueo
     * basado en el código hash para evitar el interbloqueo.
     * * **Justificación del mecanismo:** * El Deadlock ocurre porque los hilos bloquean los recursos en orden diferente (adquisición circular).
     * Para evitarlo, obligamos a que todos los hilos, sin importar el orden en que pidan r1 y r2,
     * siempre bloqueen el recurso con el hashCode más bajo primero, y luego el más alto.
     * Esto impone un orden estricto y lineal, eliminando la espera circular.
     */
    public static void adquirirRecursos(Recurso r1, Recurso r2) {
        // Ordenar bloqueos por hashCode() para evitar interbloqueos
        Recurso primero = r1.hashCode() < r2.hashCode() ? r1 : r2;
        Recurso segundo = r1.hashCode() < r2.hashCode() ? r2 : r1;

        // 1. Bloqueo del primer recurso (menor hashCode)
        synchronized (primero) {
            String nombreHilo = Thread.currentThread().getName();
            System.out.println(nombreHilo + " 🔒 bloqueó: " + primero.getNombre());

            // Pausa simulada para aumentar la probabilidad de que el otro hilo intente el bloqueo
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // 2. Bloqueo del segundo recurso (mayor hashCode)
            synchronized (segundo) {
                System.out.println(nombreHilo + " 🔒 bloqueó: " + segundo.getNombre());

                // --- Sección crítica: Usar ambos recursos ---
                r1.usar();
                r2.usar();

            } // El segundo recurso (segundo) se libera automáticamente
        }     // El primer recurso (primero) se libera automáticamente
    }

    public static void main(String[] args) throws InterruptedException {
        // 1. Definir los recursos compartidos del problema
        Recurso cuchillo = new Recurso("Cuchillo Chef");
        Recurso tabla = new Recurso("Tabla de Cortar");

        // 2. Definir las tareas (Hilos Chef)
        // Chef A: Solicita (Tabla, Cuchillo) - Orden del enunciado
        Thread chefA = new Thread(new Chef("Chef-A", tabla, cuchillo), "Chef-A");

        // Chef B: Solicita (Cuchillo, Tabla) - Orden Inverso del enunciado (Potencial de Deadlock sin ordenación)
        Thread chefB = new Thread(new Chef("Chef-B", cuchillo, tabla), "Chef-B");

        // 3. Ejecución
        System.out.println("--- Iniciando Simulación del Banquete Interminable ---");
        System.out.println("Imprimiendo HashCodes para verificar orden de bloqueo:");
        System.out.println("Cuchillo Hash: " + cuchillo.hashCode());
        System.out.println("Tabla Hash: " + tabla.hashCode());
        System.out.println("-----------------------------------------------------");

        chefA.start();
        chefB.start();

        // 4. Esperar finalización
        chefA.join();
        chefB.join();

        System.out.println("\n✅ Simulación finalizada. No se produjo Deadlock gracias al orden de bloqueo.");
    }
}
