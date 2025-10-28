package Primerexamen;

class CuentaBancaria {
    private int saldo;

    public CuentaBancaria(int saldoInicial) {
        this.saldo = saldoInicial;
    }

    public synchronized boolean retirar(int cantidad, String cajero) {
        if (saldo >= cantidad) {
            System.out.println(cajero + " va a retirar " + cantidad + "€");
            try { Thread.sleep(500); } catch (InterruptedException e) {}
            saldo -= cantidad;
            System.out.println(cajero + " retiró " + cantidad + "€. Saldo restante: " + saldo);
            return true;
        } else {
            System.out.println(cajero + " no pudo retirar (saldo insuficiente).");
            return false;
        }
    }

    public int getSaldo() {
        return saldo;
    }
}

public class Banco {
    public static void main(String[] args) {
        CuentaBancaria cuenta = new CuentaBancaria(1000);

        Runnable cajero = () -> {
            String nombre = Thread.currentThread().getName();
            for (int i = 0; i < 3; i++) {
                cuenta.retirar(400, nombre);
            }
        };

        Thread t1 = new Thread(cajero, "Cajero 1");
        Thread t2 = new Thread(cajero, "Cajero 2");
        Thread t3 = new Thread(cajero, "Cajero 3");

        t1.start(); t2.start(); t3.start();
    }
}
