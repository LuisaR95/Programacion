class Cuenta {
    private int saldo;

    public Cuenta(int saldoInicial) {
        this.saldo = saldoInicial;
    }

    public synchronized void depositar(int monto) {
        saldo += monto;
        System.out.println(Thread.currentThread().getName() + " depositó " + monto + ", saldo: " + saldo);
    }

    public synchronized void retirar(int monto) {
        if (saldo >= monto) {
            saldo -= monto;
            System.out.println(Thread.currentThread().getName() + " retiró " + monto + ", saldo: " + saldo);
        } else {
            System.out.println(Thread.currentThread().getName() + " intento retirar " + monto + ", saldo insuficiente");
        }
    }

    public int getSaldo() {
        return saldo;
    }
}

class Cajero extends Thread {
    private Cuenta cuenta;
    private int[] operaciones;

    public Cajero(Cuenta cuenta, int[] operaciones) {
        this.cuenta = cuenta;
        this.operaciones = operaciones;
    }

    public void run() {
        for (int monto : operaciones) {
            if (monto >= 0) {
                cuenta.depositar(monto);
            } else {
                cuenta.retirar(-monto);
            }
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        }
    }
}

public class BancoSimulado {
    public static void main(String[] args) throws InterruptedException {
        Cuenta cuenta = new Cuenta(1000);
        int[] ops1 = {200, -100, -50};
        int[] ops2 = {-300, 400, -100};
        int[] ops3 = {500, -200, -150};

        Thread t1 = new Cajero(cuenta, ops1);
        Thread t2 = new Cajero(cuenta, ops2);
        Thread t3 = new Cajero(cuenta, ops3);

        t1.start(); t2.start(); t3.start();
        t1.join(); t2.join(); t3.join();

        System.out.println("Saldo final: " + cuenta.getSaldo());
    }
}
