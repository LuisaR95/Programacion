package Primerexamen;//Crea tres hilos usando la interfaz Runnable que impriman un mensaje diferente.

public class Ejemplo2 implements Runnable {
    private String mensaje;

    public Ejemplo2(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName() + ": " + mensaje);
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new Ejemplo2("Soy el hilo A"));
        Thread t2 = new Thread(new Ejemplo2("Soy el hilo B"));
        Thread t3 = new Thread(new Ejemplo2("Soy el hilo C"));

        t1.start();
        t2.start();
        t3.start();
    }
}
