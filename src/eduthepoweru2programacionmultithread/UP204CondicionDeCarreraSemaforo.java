package eduthepoweru2programacionmultithread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class UP204CondicionDeCarreraSemaforo implements Runnable {
    private static long tiempoPrueba = System.currentTimeMillis() + 1000;
    private static final Semaphore semaforo = new Semaphore(5, true);
    private static final AtomicInteger contador = new AtomicInteger();
    private static final AtomicInteger contadorThreads = new AtomicInteger();
    private static final Map<String, Integer> mapa = new HashMap<>();
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        List<Thread> lista = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            lista.add(new Thread(new UP204CondicionDeCarreraSemaforo(), "thread_" + i));
            lista.get(i).start();
        }
        for (Thread h : lista) {
            try {
                h.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("\n*** Uso del semáforo por los threads ***");
        int acumulador = 0;

        lock.lock();
        try {
            for (String n : mapa.keySet()) {
                int usos = mapa.get(n);
                acumulador += usos;
                System.out.println("El thread " + n + " ha usado el semáforo " + usos + " veces.");
            }
        } finally {
            lock.unlock();
        }

        System.out.println("Número total de usos registrados en mapa: " + acumulador);
        System.out.println("Número total de usos contados globalmente: " + contadorThreads.get());
    }

    @Override
    public void run() {
        String nombre = Thread.currentThread().getName();

        while (System.currentTimeMillis() < tiempoPrueba) {
            try {
                semaforo.acquire();
                contadorThreads.incrementAndGet();

                lock.lock();
                try {
                    mapa.put(nombre, mapa.getOrDefault(nombre, 0) + 1);
                } finally {
                    lock.unlock();
                }

                System.out.println(nombre + " Valor en mapa: " + mapa.get(nombre));
                System.out.println(nombre + " Adquirido semáforo número: " + contador.incrementAndGet());

                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                contador.decrementAndGet();
                semaforo.release();
                System.out.println(nombre + " Semáforo liberado");
            }
        }
    }
}
