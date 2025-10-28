package Primerexamen;//Simula un estacionamiento con 3 plazas y 5 coches. Usa un semáforo para limitar el acceso.

import java.util.concurrent.Semaphore;

public class Ejemplo7 {
    public static void main(String[] args) {
        Semaphore plazas = new Semaphore(3); // Solo 3 plazas disponibles

        for (int i = 1; i <= 5; i++) {
            int cocheId = i;
            new Thread(() -> {
                try {
                    System.out.println("Coche " + cocheId + " esperando plaza...");
                    plazas.acquire(); // Solicita una plaza
                    System.out.println("Coche " + cocheId + " estacionó.");
                    Thread.sleep((long) (Math.random() * 3000)); // Simula tiempo estacionado
                    System.out.println("Coche " + cocheId + " sale del estacionamiento.");
                    plazas.release(); // Libera la plaza
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
