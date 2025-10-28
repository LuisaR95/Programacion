package Primerexamen;// Dos hilos incrementan un contador compartido. Muestra qué pasa con y sin sincronización.

class Contador {
    private int valor = 0;

    public synchronized void incrementar() {
        valor++;
    }

    public int getValor() {
        return valor;
    }
}

public class Ejemplo3 {
    public static void main(String[] args) throws InterruptedException {
        Contador contador = new Contador();

        Runnable tarea = () -> {
            for (int i = 0; i < 1000; i++) {
                contador.incrementar();
            }
        };

        Thread t1 = new Thread(tarea);
        Thread t2 = new Thread(tarea);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Valor final del contador: " + contador.getValor());
    }
}
