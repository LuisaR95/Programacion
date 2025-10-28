package Primerexamen;//Simula un productor que genera números y un consumidor que los lee.

class Buffer {
    private int dato;
    private boolean disponible = false;

    public synchronized void producir(int valor) throws InterruptedException {
        while (disponible) {
            wait(); // Espera hasta que el dato sea consumido
        }
        dato = valor;
        disponible = true;
        System.out.println("Producido: " + valor);
        notify(); // Despierta al consumidor
    }

    public synchronized int consumir() throws InterruptedException {
        while (!disponible) {
            wait(); // Espera hasta que haya un dato disponible
        }
        disponible = false;
        System.out.println("Consumido: " + dato);
        notify(); // Despierta al productor
        return dato;
    }
}

public class Ejemplo4 {
    public static void main(String[] args) {
        Buffer buffer = new Buffer();

        Thread productor = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    buffer.producir(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread consumidor = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    buffer.consumir();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        productor.start();
        consumidor.start();
    }
}
