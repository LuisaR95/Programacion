package Primerexamen;//Tres hilos deben ejecutar tareas simultáneamente y esperar unos a otros antes de continuar.

import java.util.concurrent.*;

public class Ejemplo8 {
    public static void main(String[] args) {
        CyclicBarrier barrera = new CyclicBarrier(3, () -> {
            System.out.println("🚀 Todos los hilos alcanzaron la barrera. Continuando...");
        });

        for (int i = 1; i <= 3; i++) {
            int id = i;
            new Thread(() -> {
                try {
                    System.out.println("Hilo " + id + " ejecutando tarea...");
                    Thread.sleep((long) (Math.random() * 2000));
                    System.out.println("Hilo " + id + " esperando en la barrera...");
                    barrera.await(); // Espera a los demás
                    System.out.println("Hilo " + id + " continúa su ejecución.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
