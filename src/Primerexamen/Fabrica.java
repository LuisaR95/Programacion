package Primerexamen;//Simula una fábrica donde los productos deben pasar por tres etapas:
//
//Ensamblado
//
//Pintura
//
//Empaque

import java.util.concurrent.*;

public class Fabrica {
    public static void main(String[] args) {
        CyclicBarrier barrera = new CyclicBarrier(3, () ->
                System.out.println("➡️ Todas las piezas completaron una etapa. Pasamos a la siguiente.\n")
        );

        for (int i = 1; i <= 3; i++) {
            int pieza = i;
            new Thread(() -> {
                try {
                    for (int etapa = 1; etapa <= 3; etapa++) {
                        System.out.println("Pieza " + pieza + " completando etapa " + etapa);
                        Thread.sleep((long) (Math.random() * 2000));
                        barrera.await();
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }).start();
        }
    }
}
