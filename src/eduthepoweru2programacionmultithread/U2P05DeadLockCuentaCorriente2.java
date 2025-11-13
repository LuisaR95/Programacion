package eduthepoweru2programacionmultithread;

// Clase que representa una cuenta corriente
class CuentaCorriente {
    private float saldo;

    public CuentaCorriente(float saldo) {
        this.saldo = saldo;
    }

    // Método sincronizado para retirar dinero
    public synchronized void retirar(float importe) {
        if (importe <= saldo) {
            saldo -= importe;
        }
    }

    // Método sincronizado para ingresar dinero
    public synchronized void ingresar(float importe) {
        if (importe > 0) {
            saldo += importe;
        }
    }

    public float getSaldo() {
        return saldo;
    }
}

// Clase principal que simula transferencias entre cuentas
public class U2P05DeadLockCuentaCorriente2 {

    /**
     * Transfiere dinero entre dos cuentas evitando el deadlock.
     * Se sincroniza siempre en el mismo orden según hashCode().
     */
    public static void transferir(CuentaCorriente origen, CuentaCorriente destino, float importe) {
        // Ordenar bloqueos por hashCode para evitar interbloqueos
        CuentaCorriente primero = origen.hashCode() < destino.hashCode() ? origen : destino;
        CuentaCorriente segundo = origen.hashCode() < destino.hashCode() ? destino : origen;

        synchronized (primero) {
            synchronized (segundo) {
                origen.retirar(importe);
                destino.ingresar(importe);

            }
        }
    }

    public static void main(String[] args) {
        // Crear dos cuentas con saldo inicial
        CuentaCorriente cc1 = new CuentaCorriente(1000);
        CuentaCorriente cc2 = new CuentaCorriente(1000);

        // Hilo 1 transfiere de cc1 a cc2
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                transferir(cc1, cc2, 10);
                try {
                    Thread.sleep(5); // pequeña pausa para simular tiempo de operación
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Hilo-1");

        // Hilo 2 transfiere de cc2 a cc1
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                transferir(cc2, cc1, 20);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Hilo-2");

        // Iniciar los hilos
        t1.start();
        t2.start();

        // Esperar que terminen
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Mostrar resultados
        System.out.println("\n=== RESULTADOS FINALES ===");
        System.out.println("Saldo final cuenta 1: " + cc1.getSaldo());
        System.out.println("Saldo final cuenta 2: " + cc2.getSaldo());
        System.out.println("Saldo total: " + (cc1.getSaldo() + cc2.getSaldo()));
    }
}
