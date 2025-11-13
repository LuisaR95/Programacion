package eduthepoweru2programacionmultithread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class U2P07ThreadsPool {
    public static void main(String[] args) {
        final int MAX_POOL_SIZE = 50;
        ExecutorService pool = Executors.newFixedThreadPool(MAX_POOL_SIZE);
        // Mapa para contar cuántas veces trabaja cada hilo
       Map<String, AtomicInteger> contadorPorHilo = new ConcurrentHashMap<>();

        // Enviar 50 tareas al pool
        for (int i = 0; i < 50; i++) {
            pool.submit(() -> {
                //añade el nombre al mapa como clave junto a contador que se va incrementando
                contadorPorHilo.computeIfAbsent(Thread.currentThread().getName(), k -> new AtomicInteger()).incrementAndGet();
                System.out.println("[" + Thread.currentThread().getName() + "] saludos");
            });
        }

        //
        pool.shutdown();
        try {
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)){
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdown();
        }
        contadorPorHilo.forEach( (key, value) -> {
            System.out.println("El Thread: " + key + " Se ha ejecutado " + value.get());
        });
        System.out.println("*** Total ejecuciones Threads: " + contadorPorHilo.values().stream().mapToInt(value ->  value.get()).sum());
    }
}