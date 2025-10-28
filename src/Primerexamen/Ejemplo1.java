package Primerexamen;// Crea un programa que inicie dos hilos. Cada hilo debe imprimir su nombre y un contador del 1 al 5 con una pequeña pausa entre números.

public class Ejemplo1 extends Thread {
    private String nombre;

    public Ejemplo1(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println(nombre + " - contador: " + i);
            try {
                Thread.sleep(500); // Pausa de medio segundo
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Ejemplo1 hilo1 = new Ejemplo1("Hilo 1");
        Ejemplo1 hilo2 = new Ejemplo1("Hilo 2");

        hilo1.start(); // Inicia ejecución en paralelo
        hilo2.start();
    }
}
