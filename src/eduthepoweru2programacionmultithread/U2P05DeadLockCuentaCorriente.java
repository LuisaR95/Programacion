package eduthepoweru2programacionmultithread;

public class U2P05DeadLockCuentaCorriente {

    // Clase interna que representa una cuenta bancaria
    static class CuentaCorriente {
        private float saldo;

        public CuentaCorriente(float saldo) {
            this.saldo = saldo;
        }

        public void retirar(float importe) {
            saldo -= importe;
        }

        public void ingresar(float importe) {
            saldo += importe;
        }

        public float getSaldo() {
            return saldo;
        }
    }

    /**
     * Método de transferencia sin riesgo de deadlock.
     * Se asegura un orden de bloqueo consistente entre cuentas.
     */
    public static void transferencia(CuentaCorriente origen, CuentaCorriente destino, float importe) {
        // Determinar orden fijo de bloqueo (por hashCode)
        CuentaCorriente cuenta1 = (origen.hashCode() < destino.hashCode()) ? origen : destino;
        CuentaCorriente cuenta2 = (origen.hashCode() < destino.hashCode()) ? destino: origen;

        synchronized (cuenta1) {
            synchronized (cuenta2) {
                origen.retirar(importe);
                destino.ingresar(importe);
            }
        }
    }

    public static void main(String[] args) {
        // Creamos dos cuentas con el mismo saldo inicial
        CuentaCorriente cc1 = new CuentaCorriente(100_000);
        CuentaCorriente cc2 = new CuentaCorriente(100_000);

        // Hilo 1 transfiere de cc1 → cc2
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1_000; i++) {
                transferencia(cc1, cc2, 10);
            }
        }, "Hilo-1");

        // Hilo 2 transfiere de cc2 → cc1
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1_000; i++) {
                transferencia(cc2, cc1, 20);
            }
        }, "Hilo-2");

        // Iniciamos ambos hilos
        t1.start();
        t2.start();

        // Esperamos que ambos hilos terminen
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Resultados finales
        System.out.println("\n--- RESULTADOS FINALES ---");
        System.out.println("Saldo cc1: " + cc1.getSaldo());
        System.out.println("Saldo cc2: " + cc2.getSaldo());
        System.out.println("Saldo total del sistema: " + (cc1.getSaldo() + cc2.getSaldo()));
    }
}
