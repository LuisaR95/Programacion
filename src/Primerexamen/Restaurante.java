package Primerexamen;//Los meseros (productores) agregan pedidos a una cola,
//y los cocineros (consumidores) los procesan uno a uno.
//
//Conceptos: BlockingQueue, comunicación segura entre hilos.

import java.util.concurrent.*;

public class Restaurante {
    public static void main(String[] args) {
        BlockingQueue<String> pedidos = new ArrayBlockingQueue<>(5);

        Runnable mesero = () -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    String pedido = "Pedido " + i + " por " + Thread.currentThread().getName();
                    pedidos.put(pedido); // Espera si la cola está llena
                    System.out.println("🧑‍💼 " + pedido + " agregado.");
                    Thread.sleep(700);
                }
            } catch (InterruptedException e) { e.printStackTrace(); }
        };

        Runnable cocinero = () -> {
            try {
                while (true) {
                    String pedido = pedidos.take(); // Espera si la cola está vacía
                    System.out.println("👨‍🍳 Cocinando " + pedido);
                    Thread.sleep(1500);
                }
            } catch (InterruptedException e) { e.printStackTrace(); }
        };

        new Thread(mesero, "Mesero 1").start();
        new Thread(mesero, "Mesero 2").start();
        new Thread(cocinero, "Cocinero").start();
    }
}
