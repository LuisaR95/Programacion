package P1;

import java.util.concurrent.ThreadLocalRandom; // Utilidad para simular tiempo

/**
 * Clase principal que contiene los recursos compartidos y la lógica de ejecución.
 * El método 'Transferir' implementa la ORDENACIÓN DE BLOQUEOS.
 */
public class PSPT1P2Corregido {

    // --- RECURSOS COMPARTIDOS (Objetos Monitor) ---

    // La clase Impresora es un recurso compartido
    static class Impresora {
        private final String nombre = "Impresora-A";
        // El método no necesita ser 'synchronized' si usamos el bloqueo externo
        public void imprimir(String doc) {
            System.out.println(Thread.currentThread().getName() + " >> IMPRIME: " + doc);
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(10, 50));
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // La clase Scanner es otro recurso compartido
    static class Scanner {
        private final String nombre = "Scanner-B";
        // El método no necesita ser 'synchronized' si usamos el bloqueo externo
        public void escanear(String doc) {
            System.out.println(Thread.currentThread().getName() + " >> ESCANEA: " + doc);
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(10, 50));
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // --- LÓGICA DE TRANSFERENCIA (Con Ordenación de Bloqueos) ---

    // Método que requiere ambos recursos y usa ordenación para evitar deadlock.
    public static void Transferir(Impresora recurso1, Scanner recurso2, String documento) {
        // La técnica de ordenación de bloqueos: siempre bloqueamos en el mismo orden.
        // Aquí usamos los hashCodes para establecer un orden arbitrario, pero consistente.
        // Es crucial que el orden sea siempre el mismo, independientemente de
        // si el hilo 1 llama (I1, S1) o el hilo 2 llama (S1, I1).

        Object primero;
        Object segundo;

        // Comprobamos si el hashCode de Impresora es menor que el de Scanner.
        // Si lo es, el orden es: Impresora -> Scanner.
        if (recurso1.hashCode() < recurso2.hashCode()) {
            primero = recurso1;
            segundo = recurso2;
        } else {
            // Si no lo es, el orden es: Scanner -> Impresora.
            // Para garantizar la consistencia, el bloqueo debe ser:
            // Bloqueo 1: el de menor hashCode. Bloqueo 2: el de mayor hashCode.
            primero = recurso2;
            segundo = recurso1;
        }

        // Hacemos un bloqueo externo de los recursos en el orden establecido
        synchronized (primero) {
            System.out.println(Thread.currentThread().getName() + " BLOQUEÓ " + ((primero == recurso1) ? recurso1.nombre : recurso2.nombre));
            try {
                Thread.sleep(10); // Pausa corta para forzar la concurrencia
            } catch (InterruptedException ignored) {}

            synchronized (segundo) {
                System.out.println(Thread.currentThread().getName() + " BLOQUEÓ " + ((segundo == recurso1) ? recurso1.nombre : recurso2.nombre));

                // Si ambos recursos están bloqueados, realizamos la operación
                recurso1.imprimir("Impresión de " + documento);
                recurso2.escanear("Escaneo de " + documento);

                // Los bloqueos se liberan automáticamente al salir de los bloques synchronized
            } // Segundo bloqueo liberado
        } // Primer bloqueo liberado
        System.out.println(Thread.currentThread().getName() + " ha FINALIZADO la transferencia de " + documento + ".\n");
    }


    // --- MÉTODO MAIN Y EJECUCIÓN ---

    public static void main(String[] args) {

        // 1. Inicialización de los recursos (una Impresora y un Scanner)
        Impresora I1 = new Impresora();
        Scanner S1 = new Scanner();

        // 2. Creación de los hilos. Ambos necesitan I1 y S1.
        Thread t1 = new Thread(() -> {
            Transferir(I1, S1, "Documento-T1");
        }, "Hilo-A"); // Hilo A pide I1, luego S1 (si el orden se mantiene)


        Thread t2 = new Thread(() -> {
            // El hilo 2 llama en orden invertido (S1, I1), pero la función Transferir
            // impone el orden consistente (menor hashCode primero)
            Transferir(I1, S1, "Documento-T2");
        }, "Hilo-B"); // Hilo B pide S1, luego I1 (si el orden se mantiene)


        // 3. Iniciar los hilos
        System.out.println("Iniciando hilos. Intentando TRANSFERIR recursos I1 y S1...");
        t1.start();
        t2.start();

        // 4. Esperar que terminen
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Programa terminado. Interbloqueo evitado.");
    }
}