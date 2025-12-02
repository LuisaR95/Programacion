package PracticaExamen;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 💰 Clase que representa una cuenta bancaria.
 */
class CuentaBancaria {
    private static final AtomicInteger generadorId = new AtomicInteger(0);
    private final int id;
    private double saldo;
    private int depositosRealizados;
    private int retirosRealizados;

    public CuentaBancaria(double saldoInicial) {
        this.id = generadorId.incrementAndGet();
        this.saldo = saldoInicial;
    }

    public synchronized void depositar(double cantidad) {
        saldo += cantidad;
        depositosRealizados++;
    }

    public synchronized void retirar(double cantidad) {
        if (cantidad <= saldo) {
            saldo -= cantidad;
            retirosRealizados++;
        }
    }

    public int getId() { return id; }
    public double getSaldo() { return saldo; }
    public int getDepositosRealizados() { return depositosRealizados; }
    public int getRetirosRealizados() { return retirosRealizados; }

    @Override
    public String toString() {
        return String.format("Cuenta{id=%d, saldo=%.2f, dep=%d, ret=%d}",
                id, saldo, depositosRealizados, retirosRealizados);
    }
}

/**
 * 🏦 Clase que representa un cajero (hilo) que atiende clientes del banco.
 */
class Cajero implements Runnable {
    private final String nombre;
    private final int clientesAtendidos;
    private final Map<Integer, CuentaBancaria> registroCuentas;
    private double dineroTotalGenerado;
    private long tiempoTotalAtencion;

    public Cajero(String nombre, int clientesAtendidos, Map<Integer, CuentaBancaria> registroCuentas) {
        this.nombre = nombre;
        this.clientesAtendidos = clientesAtendidos;
        this.registroCuentas = registroCuentas;
    }

    @Override
    public void run() {
        long inicio = System.currentTimeMillis();

        for (int i = 0; i < clientesAtendidos; i++) {
            double saldoInicial = ThreadLocalRandom.current().nextDouble(1000, 5000);
            CuentaBancaria cuenta = new CuentaBancaria(saldoInicial);

            // Simulamos operaciones aleatorias
            for (int j = 0; j < 5; j++) {
                if (ThreadLocalRandom.current().nextBoolean()) {
                    cuenta.depositar(ThreadLocalRandom.current().nextDouble(100, 1000));
                } else {
                    cuenta.retirar(ThreadLocalRandom.current().nextDouble(100, 1000));
                }
            }

            registroCuentas.put(cuenta.getId(), cuenta);
            dineroTotalGenerado += cuenta.getSaldo();

            System.out.println("💳 " + nombre + " gestionó -> " + cuenta);
        }

        tiempoTotalAtencion = System.currentTimeMillis() - inicio;
        System.out.printf("✅ %s terminó (%d clientes) en %d ms%n", nombre, clientesAtendidos, tiempoTotalAtencion);
    }

    public String getNombre() { return nombre; }
    public double getDineroTotalGenerado() { return dineroTotalGenerado; }
    public long getTiempoTotalAtencion() { return tiempoTotalAtencion; }
}

/**
 * 🏛️ Clase principal: Banco Multihilo con estadísticas avanzadas
 */
public class BancoMultihiloConEstadisticas {
    public static void main(String[] args) throws InterruptedException {
        Map<Integer, CuentaBancaria> registroCuentas = new ConcurrentHashMap<>();

        // Crear cajeros (hilos)
        Cajero c1 = new Cajero("Cajero Juan", 5, registroCuentas);
        Cajero c2 = new Cajero("Cajera Ana", 5, registroCuentas);
        Cajero c3 = new Cajero("Cajero Luis", 5, registroCuentas);

        Thread t1 = new Thread(c1);
        Thread t2 = new Thread(c2);
        Thread t3 = new Thread(c3);

        t1.start(); t2.start(); t3.start();
        t1.join(); t2.join(); t3.join();

        // ===== ESTADÍSTICAS GLOBALES =====
        double saldoTotal = registroCuentas.values().stream().mapToDouble(CuentaBancaria::getSaldo).sum();
        double saldoPromedio = registroCuentas.values().stream().mapToDouble(CuentaBancaria::getSaldo).average().orElse(0);

        CuentaBancaria cuentaMax = registroCuentas.values().stream()
                .max(Comparator.comparingDouble(CuentaBancaria::getSaldo)).orElse(null);
        CuentaBancaria cuentaMin = registroCuentas.values().stream()
                .min(Comparator.comparingDouble(CuentaBancaria::getSaldo)).orElse(null);

        long totalDepositos = registroCuentas.values().stream().mapToLong(CuentaBancaria::getDepositosRealizados).sum();
        long totalRetiros = registroCuentas.values().stream().mapToLong(CuentaBancaria::getRetirosRealizados).sum();

        // Cajero con más dinero total generado
        Cajero mejorCajero = Arrays.asList(c1, c2, c3).stream()
                .max(Comparator.comparingDouble(Cajero::getDineroTotalGenerado)).orElse(null);

        // Tiempo promedio de atención
        double tiempoPromedioCajero = Arrays.asList(c1, c2, c3).stream()
                .mapToLong(Cajero::getTiempoTotalAtencion).average().orElse(0);

        // Distribución de saldos
        Map<String, Long> distribucion = new TreeMap<>();
        for (CuentaBancaria c : registroCuentas.values()) {
            String rango;
            if (c.getSaldo() < 2000) rango = "< 2000";
            else if (c.getSaldo() < 4000) rango = "2000 - 3999";
            else if (c.getSaldo() < 6000) rango = "4000 - 5999";
            else rango = "≥ 6000";
            distribucion.merge(rango, 1L, Long::sum);
        }

        // ===== RESULTADOS =====
        System.out.println("\n===== 🏦 ESTADÍSTICAS DEL BANCO =====");
        System.out.println("Total cuentas creadas: " + registroCuentas.size());
        System.out.printf("Saldo total del banco: %.2f%n", saldoTotal);
        System.out.printf("Saldo promedio por cuenta: %.2f%n", saldoPromedio);
        System.out.printf("Total de depósitos realizados: %d%n", totalDepositos);
        System.out.printf("Total de retiros realizados: %d%n", totalRetiros);
        System.out.printf("Promedio de operaciones por cuenta: %.2f%n",
                (double)(totalDepositos + totalRetiros) / registroCuentas.size());

        if (cuentaMax != null && cuentaMin != null) {
            System.out.println("\n💎 Cuenta con mayor saldo: " + cuentaMax);
            System.out.println("⚙️  Cuenta con menor saldo: " + cuentaMin);
        }

        System.out.println("\n💰 Cajero con más dinero generado: " +
                (mejorCajero != null ? mejorCajero.getNombre() + " (total: " +
                        String.format("%.2f", mejorCajero.getDineroTotalGenerado()) + ")" : "N/A"));

        System.out.printf("🕓 Tiempo promedio de atención por cajero: %.2f ms%n", tiempoPromedioCajero);

        System.out.println("\n📊 Distribución de saldos:");
        distribucion.forEach((rango, cantidad) ->
                System.out.println(" - " + rango + ": " + cantidad + " cuentas"));

        System.out.println("\n✅ Fin del procesamiento multihilo bancario.");
    }
}
