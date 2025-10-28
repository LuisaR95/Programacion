import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoolTareas {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= 10; i++) {
            int num = i;
            pool.submit(() -> {
                System.out.println("Procesando tarea " + num);
                try { Thread.sleep(1000); } catch (InterruptedException e) {}
            });
        }

        pool.shutdown();
        while (!pool.isTerminated()) {} // espera a que todas terminen
        System.out.println("Todas las tareas completadas.");
    }
}
